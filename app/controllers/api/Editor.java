package controllers.api;

import static play.libs.Json.toJson;
import game.json.excludes.MeshExcludeGeometryMixin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import models.entity.game.Environment;
import models.entity.game.GeoMaterial;
import models.entity.game.Geometry;
import models.entity.game.GeometryType;
import models.entity.game.Map;
import models.entity.game.Material;
import models.entity.game.Mesh;
import models.entity.game.Path;
import models.entity.game.Waypoint;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import play.Logger;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import controllers.utils.EnvironmentComparator;
import dao.game.EnvironmentDAO;
import dao.game.GeometryDAO;
import dao.game.MapDAO;
import dao.game.MaterialDAO;
import dao.game.MeshDAO;

public class Editor extends Controller {

  private static final Logger.ALogger log = Logger.of(Editor.class);

  public static Result mapDefault() {
    Map map = new Map();
    map.getTerrain().getGeometry().setType(GeometryType.TERRAIN);
    map.getTerrain().getGeometry().getMetadata().setGeneratedBy("WorldEditor");
    return ok(toJson(map));
  }

  @Transactional
  public static Result mapShow(Long id) {
    Map map = MapDAO.getMapById(id);
    if (map != null) {
      ObjectMapper mapper = new ObjectMapper();
      mapper.getSerializationConfig().addMixInAnnotations(Mesh.class, MeshExcludeGeometryMixin.class);
      MapDAO.mapMaterials(map);
      MapDAO.mapStaticGeometries(map);
      try {
        return ok(mapper.writeValueAsString(map));
      } catch (IOException e) {
        log.error("Failed to serialize root environment:", e);
      }

      return badRequest("Unexpected error occurred");
    }
    return notFound();
  }

  @Transactional
  public static Result envShow(Long id) {
    List<Environment> envs = EnvironmentDAO.getEnvWithGeometries(id);
    Collections.sort(envs, new EnvironmentComparator());
    return ok(toJson(envs));
  }

  @BodyParser.Of(value = BodyParser.Json.class, maxLength = 52428800)
  @Transactional
  public static Result addMap() {
    log.info("Saving MAP");
    if (request().body().isMaxSizeExceeded()) {
      return badRequest("Too much data!");
    }
    JsonNode mapNode = request().body().asJson();
    if (mapNode == null) {
      return badRequest("Failed to parse json request");
    }
    ObjectMapper mapper = new ObjectMapper();
    Map map = null;
    try {
      map = mapper.readValue(mapNode, Map.class);
    } catch (IOException e) {
      String errorMessage = "Failed to parse request data to entity";
      log.error(errorMessage, e);
      return badRequest(errorMessage);
    }
    if (map == null || !isValid(map)) {
      String errorMessage = "Failed to parse request data to entity";
      return badRequest(errorMessage);
    }
    saveMap(map);
    // Flushing for new Id
    JPA.em().flush();
    log.info(String.format("Map Id <%s> successfully saved!", map.getId()));
    return ok(toJson(map.getId()));
  }

  private static boolean isValid(Map map) {
    if (map == null || map.getTerrain() == null || map.getTerrain().getGeometry() == null || map.getTerrain().getGeometry().getMetadata() == null) {
      return false;
    }
    return true;
  }

  private static void saveMap(Map map) {
    Map oldMap = null;
    if (map.getId() != null) {
      oldMap = MapDAO.getMapById(map.getId());
    }
    if (map.getTerrain().getGeometry().getMetadata().getGeometry() == null) {
      map.getTerrain().getGeometry().getMetadata().setGeometry(map.getTerrain().getGeometry());
    }
    if (map.getTerrain().getGeometry().getType() == null) {
      map.getTerrain().getGeometry().setType(GeometryType.TERRAIN);
    }
    if (map.getTerrain().getMap() == null) {
      map.getTerrain().setMap(map);
    }
    if (map.getObjects() != null) {
      List<Long> oldIds = new ArrayList<Long>();
      Set<Mesh> meshes = new HashSet<Mesh>();
      for (Mesh mesh : map.getObjects()) {
        if (mesh.getGeometry().getId() != null) {
          mesh.setMap(map);
          mesh.setGeometry(GeometryDAO.getInstance().findUnique(mesh.getGeometry().getId()));
        }
        if (mesh.getId() == null || mesh.getId().intValue() < 0) {
          mesh.setId(null);
        } else {
          mesh = MeshDAO.getInstance().merge(mesh);
          oldIds.add(mesh.getId());
        }
        meshes.add(mesh);
      }
      // Delete old meshes

      for (Iterator<Mesh> iterator = map.getObjects().iterator(); iterator.hasNext();) {
        Mesh mesh = iterator.next();
        if (!oldIds.contains(mesh.getId())) {
          iterator.remove();
          JPA.em().remove(mesh);
        }

      }
      map.setObjects(meshes);
    }

    if (map.getPaths() != null) {
      Set<Path> paths = new HashSet<Path>();
      for (Path path : map.getPaths()) {
        Set<Waypoint> waypoints = new HashSet<Waypoint>();
        Set<Long> oldWaypointIds = new HashSet<Long>();
        Set<Long> oldPathIds = new HashSet<Long>();
        path.setMap(map);
        for (Waypoint waypoint : path.getWaypoints()) {
          waypoint.setPath(path);
          if (waypoint.getId() != null && waypoint.getId().longValue() > -1) {
            waypoint = JPA.em().merge(waypoint);
            oldWaypointIds.add(waypoint.getId());
          } else {
            waypoint.setId(null);
          }
          waypoints.add(waypoint);
        }

        path.setWaypoints(waypoints);
        if (path.getId() != null && path.getId().longValue() > -1) {
          path = JPA.em().merge(path);
          oldPathIds.add(path.getId());
        } else {
          path.setId(null);
        }
        paths.add(path);
        // Remove old stuff ...
        if (oldMap != null) {
          for (Iterator<Path> pathIt = oldMap.getPaths().iterator(); pathIt.hasNext();) {
            Path oldPath = pathIt.next();
            if (!oldPathIds.contains(oldPath.getId())) {
              pathIt.remove();
              JPA.em().remove(oldPath);
            } else {
              for (Iterator<Waypoint> wayIt = oldPath.getWaypoints().iterator(); wayIt.hasNext();) {
                Waypoint oldWaypoint = wayIt.next();
                if (!oldWaypointIds.contains(oldWaypoint.getId())) {
                  wayIt.remove();
                  JPA.em().remove(oldWaypoint);
                }
              }
            }
          }

        }
      }
      map.setPaths(paths);

    }

    // For saving MatGeoId.materialId is the index of map.getMaterials()!
    java.util.Map<Integer, Material> matMap = saveMaterials(map);
    saveGeometryMaterials(map.getTerrain().getGeometry(), matMap);
    if (oldMap == null) {
      JPA.em().persist(map);
    } else {
      map = JPA.em().merge(map);
    }
  }

  // Mapping Indexes
  private static void saveGeometryMaterials(Geometry geometry, java.util.Map<Integer, Material> mapping) {
    if (geometry.getMatIdMapper() != null) {
      if (geometry.getGeoMaterials() == null) {
        geometry.setGeoMaterials(new HashSet<GeoMaterial>());
      }
      GeometryDAO.createGeoMaterials(geometry, mapping);
    }
  }

  private static java.util.Map<Integer, Material> saveMaterials(Map map) {

    if (map.getAllMaterials() == null) {
      map.setAllMaterials(new HashSet<Material>());
    }
    java.util.Map<Integer, Material> result = MaterialDAO.mapAndSave(map.getMaterials());
    for (Material mat : result.values()) {
      map.getAllMaterials().add(mat);
    }
    return result;
  }

}
