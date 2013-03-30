package common.controllers;

import static play.libs.Json.toJson;

import java.util.List;

import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

import common.models.User;

/**
 * The UserController handle api requests for the user object.
 * 
 * @author Sebastian Sachtleben
 */
public class Users extends Controller {

  public static Result list() {
    List<User> users = User.getFinder().all();
    return ok(toJson(users));
  }

  public static Result show(Long id) {
    User user = User.getFinder().where().eq("id", id).findUnique();
    return ok(toJson(user));
  }

  public static Result add() {
    User user = Form.form(User.class).bindFromRequest().get();
    user.save();
    return ok(toJson(user));
  }

}
