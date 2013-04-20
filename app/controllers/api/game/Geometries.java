package controllers.api.game;

import static play.libs.Json.toJson;

import java.util.List;

import models.entity.game.Geometry;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.mvc.Result;
import controllers.api.BaseAPI;

/**
 * The Geometries controller handle api requests for the Geometry model.
 * 
 * @author Sebastian Sachtleben
 */
public class Geometries extends BaseAPI<Long, Geometry> {

  private Geometries() {
    super(Long.class, Geometry.class);
  }

  public static final Geometries instance = new Geometries();

  @Transactional
  public static Result list() {
    List<Geometry> list = instance.listAll();
    return ok(toJson(list));
  }

  @Transactional
  public static Result show(Long id) {
    return instance.showEntry(id);
  }

  @Transactional
  public static Result update(Long id) {
    Geometry geometry = instance.findUnique(id);
    geometry = JPA.em().merge(geometry);
    return ok(toJson(geometry));
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
