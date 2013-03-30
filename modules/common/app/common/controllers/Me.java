package common.controllers;

import static play.libs.Json.toJson;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

import common.models.User;
import common.providers.UsernamePasswordAuthProvider;
import common.providers.UsernamePasswordAuthProvider.MyLogin;

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
  
  public static Result login() {
    Logger.info("login called");
    com.feth.play.module.pa.controllers.Authenticate.noCache(response());
    final Form<MyLogin> filledForm = UsernamePasswordAuthProvider.LOGIN_FORM.bindFromRequest();
    if (filledForm.hasErrors()) {
      return badRequest();
    } else {
      return UsernamePasswordAuthProvider.handleLogin(ctx());
    }
  }
  
  public static Result signup() {
    Logger.info("signup called");
    return badRequest();
  }
  
}
