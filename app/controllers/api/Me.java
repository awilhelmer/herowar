package controllers.api;

import static play.libs.Json.toJson;
import models.api.error.AuthenticationError;
import models.api.error.FormValidationError;
import models.entity.User;
import play.Logger;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import providers.FormLogin;
import providers.FormSignup;
import providers.UsernamePasswordAuthProvider;

import com.feth.play.module.pa.controllers.Authenticate;

import controllers.Application;

/**
 * The Me controller allows our application to get information about the current
 * logged in user.
 * 
 * @author Sebastian Sachtleben
 */
public class Me extends Controller {

  public static Result show() {
    User user = getLoggedInUser();
    if (user != null) {
      return ok(toJson(getLoggedInUser()));
    }
    return ok("{}");
  }

  public static Result login() {
    Logger.info("login called");
    com.feth.play.module.pa.controllers.Authenticate.noCache(response());
    final Form<FormLogin> filledForm = UsernamePasswordAuthProvider.LOGIN_FORM.bindFromRequest();

    // Validate form data
    if (filledForm.hasErrors()) {
      return badRequest(toJson(new FormValidationError(filledForm.errorsAsJson())));
    }

    // Check if user exists
    if (User.getFinder().where().eq("username", filledForm.get().getEmail()).findUnique() == null) {
      return badRequest(toJson(new AuthenticationError()));
    }

    // Handle login
    Result loginResult = UsernamePasswordAuthProvider.handleLogin(ctx());

    // Check if user really logged in
    if (getLoggedInUser() == null) {
      return badRequest(toJson(new AuthenticationError()));
    }
    return loginResult;
  }

  public static Result logout() {
    Authenticate.logout();
    return ok();
  }

  public static Result signup() {
    Logger.info("signup called");
    com.feth.play.module.pa.controllers.Authenticate.noCache(response());
    final Form<FormSignup> filledForm = UsernamePasswordAuthProvider.SIGNUP_FORM.bindFromRequest();
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
