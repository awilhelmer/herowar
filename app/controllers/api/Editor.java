package controllers.api;

import static play.libs.Json.toJson;

import java.io.IOException;

import models.entity.game.GeometryType;
import models.entity.game.Map;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import play.Logger;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

public class Editor extends Controller {

  private static final Logger.ALogger log = Logger.of(Editor.class);

  @BodyParser.Of(value = BodyParser.Json.class, maxLength = 52428800)
  public static Result addMap() {
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
    if (map.getTerrain().getGeometry().getMetadata().getId() == null || map.getTerrain().getGeometry().getMetadata().getId() == 0) {
      map.getTerrain().getGeometry().getMetadata().save();
    }
    if (map.getTerrain().getGeometry().getId() == null || map.getTerrain().getGeometry().getId() == 0) {
      map.getTerrain().getGeometry().save();
    }
    if (map.getTerrain().getId() == null || map.getTerrain().getId() == 0) {
      map.getTerrain().save();
    }
    if (map.getId() == null || map.getId() == 0) {
      map.save();
    }
    if (map.getTerrain().getGeometry().getMetadata().getGeometry() == null) {
      map.getTerrain().getGeometry().getMetadata().setGeometry(map.getTerrain().getGeometry());
      map.getTerrain().getGeometry().getMetadata().save();
    }
    if (map.getTerrain().getGeometry().getType() == null) {
      map.getTerrain().getGeometry().setType(GeometryType.TERRAIN);
      map.getTerrain().getGeometry().save();
    }
    if (map.getTerrain().getMap() == null) {
      map.getTerrain().setMap(map);
      map.getTerrain().save();
    }
  }

}
