package common.controllers;

import static play.libs.Json.toJson;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

import common.models.api.error.FormValidationError;
import common.models.entity.User;
import common.providers.UsernamePasswordAuthProvider;
import common.providers.UsernamePasswordAuthProvider.LoginForm;
import common.providers.UsernamePasswordAuthProvider.SignupForm;

/**
 * The Me controller allows our application to get information about the current
 * logged in user.
 * 
 * @author Sebastian Sachtleben
 */
public class Me extends Controller {

  public static Result show() {
    return ok(toJson(getLoggedInUser()));
  }

  public static Result login() {
    Logger.info("login called");
    com.feth.play.module.pa.controllers.Authenticate.noCache(response());
    final Form<LoginForm> filledForm = UsernamePasswordAuthProvider.LOGIN_FORM.bindFromRequest();
    if (filledForm.hasErrors()) {
      return badRequest(toJson(new FormValidationError(filledForm.errorsAsJson())));
    }
    return UsernamePasswordAuthProvider.handleLogin(ctx());
  }

  public static Result signup() {
    Logger.info("signup called");
    com.feth.play.module.pa.controllers.Authenticate.noCache(response());
    final Form<SignupForm> filledForm = UsernamePasswordAuthProvider.SIGNUP_FORM.bindFromRequest();
    if (filledForm.hasErrors()) {
      return badRequest(toJson(new FormValidationError(filledForm.errorsAsJson())));
    }
    return UsernamePasswordAuthProvider.handleSignup(ctx());
  }
  
  public static Result checkUsername(String username) {
    return ok(toJson(User.getFinder().where().eq("username", username).findUnique() != null));
  }
  
  public static Result checkEmail(String email) {
    return ok(toJson(User.getFinder().where().eq("email", email).findUnique() != null));
  }

  private static User getLoggedInUser() {
    return Application.getLocalUser(session());
  }

}
