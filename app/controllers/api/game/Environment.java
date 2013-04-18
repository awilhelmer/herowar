package controllers.api.game;

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

  public static Result list() {
    return instance.listAll();
  }

  public static Result show(Long id) {
    return instance.showEntry(id);
  }

}
