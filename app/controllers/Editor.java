package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.editor.index;


/**
 * 
 * @author Alexander Wilhelmer
 *
 */
public class Editor extends Controller {

  public static Result index() {
    return ok(index.render());
  }
  
  public static Result viewer() {
    return ok(index.render());
  }
}
