package controllers.api;

import play.Logger;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

public class Editor extends Controller {

  private static final Logger.ALogger log = Logger.of(Editor.class);
  
  @BodyParser.Of(value = BodyParser.FormUrlEncoded.class, maxLength = 52428800)
  public static Result addMap() {
    if(request().body().isMaxSizeExceeded()) {
      return badRequest("Too much data!");
    } 
    log.info("getMap by empty string " + request().body().asFormUrlEncoded().toString());
    return ok();
  }
  
}
