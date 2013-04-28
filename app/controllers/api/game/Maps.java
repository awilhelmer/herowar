package controllers.api.game;

import static play.libs.Json.toJson;
import models.entity.game.Map;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.mvc.Result;
import controllers.api.BaseAPI;

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

  @Transactional
  public static Result list() {

    return instance.listAll();
  }

  @Transactional
  public static Result show(Long id) {
    
    
    return instance.showEntry(id);
  }

  @Transactional
  public static Result update(Long id) {
    Map map = instance.findUnique(id);
    map = JPA.em().merge(map);
    return ok(toJson(map));
  }

  @Transactional
  public static Result delete(Long id) {
    return instance.deleteEntry(id);
  }

  @Transactional
  public static Result add() {
    return instance.addEntry();
  }
}
