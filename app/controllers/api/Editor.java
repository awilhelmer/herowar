package controllers.api;

import java.io.IOException;

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
    if(request().body().isMaxSizeExceeded()) {
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
      log.error("Failed to parse request data to entity");
      e.printStackTrace();
    }
    log.info("Map: " + map.toString());
    log.info("Terrain: " + map.getTerrain().toString());
    log.info("Geometry: " + map.getTerrain().getGeometry().toString());
    return ok();
  }
  
}
