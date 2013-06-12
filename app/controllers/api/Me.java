package controllers.api;

import static play.libs.Json.toJson;
import game.json.excludes.GameResultExcludeMapDataMixin;

import java.io.IOException;

import models.api.error.AuthenticationError;
import models.api.error.FormValidationError;
import models.entity.User;
import models.entity.game.Map;

import org.codehaus.jackson.map.ObjectMapper;

import play.Logger;
import play.data.Form;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import providers.FormLogin;
import providers.FormSignup;
import providers.UsernamePasswordAuthProvider;

import com.feth.play.module.pa.controllers.Authenticate;

import controllers.Application;
import dao.UserDAO;

/**
 * The Me controller allows our application to get information about the current
 * logged in user.
 * 
 * @author Sebastian Sachtleben
 */
public class Me extends Controller {
  private static final Logger.ALogger log = Logger.of(Me.class);

  @Transactional
  public static Result show() {
    User user = getLoggedInUser();
    if (user != null) {
      ObjectMapper mapper = new ObjectMapper();
      mapper.getSerializationConfig().addMixInAnnotations(Map.class, GameResultExcludeMapDataMixin.class);
      try {
        return ok(mapper.writeValueAsString(user));
      } catch (IOException e) {
        log.error("Failed to create me json:", e);
      }
    }
    return ok("{}");
  }

  @Transactional
  public static Result login() {
    Logger.info("login called");
    com.feth.play.module.pa.controllers.Authenticate.noCache(response());
    final Form<FormLogin> filledForm = UsernamePasswordAuthProvider.LOGIN_FORM.bindFromRequest();

    // Validate form data
    if (filledForm.hasErrors()) {
      return badRequest(toJson(new FormValidationError(filledForm.errorsAsJson())));
    }

    // Check if user exists
    if (UserDAO.findByUsername(filledForm.get().getEmail()) == null) {
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

  @Transactional
  public static Result signup() {
    Logger.info("signup called");
    com.feth.play.module.pa.controllers.Authenticate.noCache(response());
    final Form<FormSignup> filledForm = UsernamePasswordAuthProvider.SIGNUP_FORM.bindFromRequest();
    if (filledForm.hasErrors()) {
      return badRequest(toJson(new FormValidationError(filledForm.errorsAsJson())));
    }
    return UsernamePasswordAuthProvider.handleSignup(ctx());
  }

  @Transactional
  public static Result checkUsername(String username) {
    return ok(toJson(UserDAO.findByUsername(username) != null));
  }

  @Transactional
  public static Result checkEmail(String email) {
    return ok(toJson(UserDAO.findByUserEmail(email) != null));
  }

  private static User getLoggedInUser() {
    return Application.getLocalUser(session());
  }

}
