package controllers.api;

import static play.libs.Json.toJson;
import models.entity.User;
import play.data.Form;
import play.mvc.Result;

/**
 * The Users controller handle api requests for the User model.
 * 
 * @author Sebastian Sachtleben
 */
public class Users extends BaseAPI<User, Long> {

  private Users() {
    super(User.class, Long.class);
  }

  public static Users instance = new Users();

  public static Result list() {
    return instance.listAll();
  }

  public static Result show(Long id) {
    User user = User.getFinder().where().eq("id", id).findUnique();
    return ok(toJson(user));
  }

  public static Result update(Long id) {
    User user = User.getFinder().where().eq("id", id).findUnique();
    User.merge(user, Form.form(User.class).bindFromRequest().get());
    return ok(toJson(user));
  }

  public static Result delete(Long id) {
    User user = User.getFinder().where().eq("id", id).findUnique();
    User.delete(user);
    return ok("{}");
  }

  public static Result add() {
    User user = Form.form(User.class).bindFromRequest().get();
    user.save();
    return ok(toJson(user));
  }

}
