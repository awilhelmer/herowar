package controllers.api.game;

import static play.libs.Json.toJson;

import java.util.List;

import play.db.jpa.Transactional;
import play.mvc.Result;
import controllers.api.BaseAPI;

/**
 * The Environment controller handle api requests for the Environment model.
 * 
 * @author Sebastian Sachtleben
 */
public class Environment extends BaseAPI<Long, models.entity.game.Environment> {

  private Environment() {
    super(Long.class, models.entity.game.Environment.class);
  }

  public static final Environment instance = new Environment();

  @Transactional
  public static Result list() {
    List<models.entity.game.Environment> list = instance.listAll(); 
    return ok(toJson(list));
  }

  @Transactional
  public static Result show(Long id) {
    return instance.showEntry(id);
  }

}
