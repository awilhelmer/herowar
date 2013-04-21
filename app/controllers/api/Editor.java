package controllers.api;

import static play.libs.Json.toJson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import models.entity.game.Environment;
import models.entity.game.GeoMaterial;
import models.entity.game.Geometry;
import models.entity.game.GeometryType;
import models.entity.game.Map;
import models.entity.game.Material;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import play.Logger;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import dao.game.EnvironmentDAO;
import dao.game.MapDAO;
import dao.game.MaterialDAO;

public class Editor extends Controller {

  private static final Logger.ALogger log = Logger.of(Editor.class);

  public static Result mapDefault() {
    return ok(toJson(new Map()));
  }

  @Transactional
  public static Result mapShow(Long id) {
    Map map = MapDAO.getMapById(id);
    mapMaterials(map);
    return ok(toJson(map));
  }

  @Transactional
  public static Result envShow(Long id) {
    Set<Environment> envs = EnvironmentDAO.getEnvWithGeometries(id);
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
    return ok(toJson(map));
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
    java.util.Map<Integer, Material> matMap = saveMaterials(map);
    saveGeometryMaterials(map.getTerrain().getGeometry(), matMap);
    JPA.em().persist(map);
  }

  // Mapping Indexes
  private static void saveGeometryMaterials(Geometry geometry, java.util.Map<Integer, Material> mapping) {
    if (geometry.getMaterialsIndex() != null) {
      if (geometry.getGeoMaterials() == null) {
        geometry.setGeoMaterials(new HashSet<GeoMaterial>());
      }
      for (Integer index : geometry.getMaterialsIndex()) {
        if (mapping.containsKey(index)) {
          Material mat = mapping.get(geometry.getMaterialsIndex());
          GeoMaterial geoMat = new GeoMaterial();
          geoMat.setId(new GeoMaterial.PK());
          geoMat.getId().setMaterial(mat);
          geoMat.getId().setGeometry(geometry);
          geoMat.setArrayIndex(Integer.valueOf(index).longValue());
          geometry.getGeoMaterials().add(geoMat);
        } else {
          log.error(String.format("Material Index <%s> not found in Map->allMaterials!", index));
        }
      }
    }
  }

  private static java.util.Map<Integer, Material> saveMaterials(Map map) {
    java.util.Map<Integer, Material> result = new HashMap<Integer, Material>();
    if (map.getAllMaterials() == null) {
      map.setAllMaterials(new HashSet<Material>());
    }
    for (int i = 0; i < map.getMaterials().size(); i++) {
      Material mat = map.getMaterials().get(i);
      result.put(i, mat);
      Material dbMat = MaterialDAO.getMaterialbyId(mat.getId());
      if (dbMat == null) {
        dbMat = mat;
        JPA.em().persist(dbMat);
      } else {
        dbMat = MaterialDAO.mergeMaterial(mat);
      }
    }
    return result;
  }

  private static void mapMaterials(Map map) {
    map.setMaterials(new ArrayList<Material>(map.getAllMaterials()));
    Geometry geo = map.getTerrain().getGeometry();
    geo.setMaterialsIndex(new ArrayList<Integer>());
    for (GeoMaterial geoMat : geo.getGeoMaterials()) {
      Material mat = geoMat.getId().getMaterial();
      int index = map.getMaterials().indexOf(mat);
      if (index > -1 && index == geoMat.getArrayIndex().intValue()) {
        geo.getMaterialsIndex().add(index);
      } else {
        log.error(String.format("Material Index <%s> and Geometry Index <%s> arent the same!", index, geoMat.getArrayIndex()));
      }

    }
    Collections.sort(geo.getMaterialsIndex()); // sorted indices
  }

}
