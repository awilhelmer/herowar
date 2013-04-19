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
public class Users extends BaseAPI<Long, User> {

  private Users() {
    super(Long.class, User.class);
  }

  public static final Users instance = new Users();

  public static Result list() {
    return instance.listAll();
  }

  public static Result show(Long id) {
    return instance.showEntry(id);
  }

  public static Result update(Long id) {
    User user = instance.merge(Form.form(User.class).bindFromRequest().get());
    return ok(toJson(user));
  }

  public static Result delete(Long id) {
    return instance.deleteEntry(id);
  }

  public static Result add() {
    return instance.addEntry();
  }

}
