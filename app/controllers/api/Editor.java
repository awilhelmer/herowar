package controllers.api;

import static play.libs.Json.toJson;
import game.json.excludes.MeshExcludeGeometryMixin;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import models.entity.game.Environment;
import models.entity.game.GeoMaterial;
import models.entity.game.Geometry;
import models.entity.game.GeometryType;
import models.entity.game.Map;
import models.entity.game.Material;
import models.entity.game.Mesh;

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
      MapDAO.mapWaves(map);
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

    if (map.getTerrain().getGeometry().getMetadata().getGeometry() == null) {
      map.getTerrain().getGeometry().getMetadata().setGeometry(map.getTerrain().getGeometry());
    }
    if (map.getTerrain().getGeometry().getType() == null) {
      map.getTerrain().getGeometry().setType(GeometryType.TERRAIN);
    }
    if (map.getTerrain().getMap() == null) {
      map.getTerrain().setMap(map);
    }
    Set<Long> oldMeshIds = new HashSet<Long>();
    Set<Long> oldWaypointIds = new HashSet<Long>();
    Set<Long> oldPathIds = new HashSet<Long>();
    Set<Long> oldWaveIds = new HashSet<Long>();

    if (map.getObjects() != null) {
      MapDAO.createMeshes(map, oldMeshIds);
    }

    if (map.getPaths() != null) {
      MapDAO.createPaths(map, oldPathIds, oldWaypointIds);
    }
    if (map.getWaves() != null) {
      MapDAO.createWaves(map, oldWaveIds);
    }

    // For saving MatGeoId.materialId is the index of map.getMaterials()!
    java.util.Map<Integer, Material> matMap = saveMaterials(map);
    saveGeometryMaterials(map.getTerrain().getGeometry(), matMap);
    if (map.getId() == null) {
      JPA.em().persist(map);
    } else {
      map = JPA.em().merge(map);
    }

    MapDAO.deletePaths(map, oldPathIds, oldWaypointIds);
    MapDAO.deleteWaves(map, oldWaveIds);
    MapDAO.deleteMeshes(map, oldMeshIds);

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
