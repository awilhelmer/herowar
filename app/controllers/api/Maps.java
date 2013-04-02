package controllers.api;

import static play.libs.Json.toJson;
import models.entity.game.Map;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;


/**
 * The Maps controller handle api requests for the Map model.
 * 
 * @author Sebastian Sachtleben
 */
public class Maps extends Controller {

  public static Result list() {
    return ok(toJson(Map.getFinder().all()));
  }

  public static Result show(Long id) {
    Map map = Map.getFinder().where().eq("id", id).findUnique();
    return ok(toJson(map));
  }
  
  public static Result update(Long id) {
    Map map = Map.getFinder().where().eq("id", id).findUnique();
    Map.merge(map, Form.form(Map.class).bindFromRequest().get());
    return ok(toJson(map));
  }
  
  public static Result delete(Long id) {
    Map map = Map.getFinder().where().eq("id", id).findUnique();
    map.delete();
    return ok("{}");
  }

  public static Result add() {
    Map map = Form.form(Map.class).bindFromRequest().get();
    map.save();
    return ok(toJson(map));
  }

}
