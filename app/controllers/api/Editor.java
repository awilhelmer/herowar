package controllers.api;

import static play.libs.Json.toJson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import models.entity.game.Environment;
import models.entity.game.GeoMaterial;
import models.entity.game.Geometry;
import models.entity.game.GeometryType;
import models.entity.game.Map;
import models.entity.game.Material;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import controllers.utils.EnvironmentComparator;

import play.Logger;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;
import dao.game.EnvironmentDAO;
import dao.game.GeometryDAO;
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
    // For saving MatGeoId.materialId is the index of map.getMaterials()!
    java.util.Map<Integer, Material> matMap = saveMaterials(map);
    saveGeometryMaterials(map.getTerrain().getGeometry(), matMap);
    if (map.getId() == null) {
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

  /**
   * For loading the MatGeoId.materialId is the real DB id
   * 
   * @param map
   */
  private static void mapMaterials(Map map) {
    map.setMaterials(new ArrayList<Material>(map.getAllMaterials()));
    Geometry geo = map.getTerrain().getGeometry();
    GeometryDAO.mapMaterials(geo);
  }

}
