package controllers.common;

import java.util.List;

import models.common.User;

import play.db.ebean.Model;
import play.mvc.Controller;
import play.mvc.Result;

import static play.libs.Json.toJson;

public class UserController extends Controller {
  
  public static Result list() {
    List<User> users = new Model.Finder(Long.class, User.class).all();
    return ok(toJson(users));
  }
  
  public static Result show(Long id) {
    User user = (User) new Model.Finder(Long.class, User.class).where().eq("id", id).findUnique();
    return ok(toJson(user));    
  }

}
