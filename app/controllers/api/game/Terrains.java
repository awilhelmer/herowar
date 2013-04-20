package controllers.api.game;

import static play.libs.Json.toJson;
import controllers.api.BaseAPI;
import models.entity.game.Terrain;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.mvc.Result;

/**
 * The Terrains controller handle api requests for the Terrain model.
 * 
 * @author Sebastian Sachtleben
 */
public class Terrains extends BaseAPI<Long, Terrain> {

  private Terrains() {
    super(Long.class, Terrain.class);
  }

  public static final Terrains instance = new Terrains();

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
    Terrain terrain = instance.findUnique(id);
    terrain = JPA.em().merge(terrain);
    return ok(toJson(terrain));
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
