package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.editor.index;

public class Editor extends Controller {

  public static Result index() {
    return ok(index.render());
  }
}
