package controllers.api;

import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

public class Editor extends Controller {

  private static final Logger.ALogger log = Logger.of(Editor.class);
  
  public static Result addMap() {
    log.info(Form.form().toString());
    return ok();
  }
  
}