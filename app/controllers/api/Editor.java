package controllers.api;

import org.codehaus.jackson.JsonNode;

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
    JsonNode map = request().body().asJson();
    if (map == null) {
      return badRequest("Failed to parse json request");
    }
    log.info("Map Id: " + map.get("id").asInt());
    return ok();
  }
  
}
