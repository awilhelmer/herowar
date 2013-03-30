package common.controllers;

import static play.libs.Json.toJson;
import play.mvc.Controller;
import play.mvc.Result;

import common.models.User;

/**
 * The Me controller allows our application to get information about the current logged in user.
 * 
 * @author Sebastian Sachtleben
 */
public class Me extends Controller {

  public static Result show() {
    final User user = Application.getLocalUser(session());
    return ok(toJson(user));
  }
  
}
