package controllers;

import play.mvc.Controller;
import play.mvc.Result;

import views.html.index;

/**
 * The Application controller handles request for "/" pattern.
 * 
 * @author Sebastian Sachtleben
 */
public class Application extends Controller {

  public static Result index() {
    return ok(index.render());
  }

}
