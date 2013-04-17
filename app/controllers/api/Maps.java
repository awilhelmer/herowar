package controllers.api;

import static play.libs.Json.toJson;
import models.entity.game.Map;
import play.data.Form;
import play.mvc.Result;


/**
 * The Maps controller handle api requests for the Map model.
 * 
 * @author Sebastian Sachtleben
 */
public class Maps extends BaseAPI<Long, Map> {
  
  private Maps() {
    super(Long.class, Map.class);
  }

  public static final Maps instance = new Maps();

  public static Result list() {
    return instance.listAll();
  }

  public static Result show(Long id) {
    return instance.showEntry(id);
  }

  public static Result update(Long id) {
    Map map = instance.findUnique(id);
    Map.merge(map, Form.form(Map.class).bindFromRequest().get());
    return ok(toJson(map));
  }

  public static Result delete(Long id) {
    return instance.deleteEntry(id);
  }

  public static Result add() {
    return instance.addEntry();
  }
}
