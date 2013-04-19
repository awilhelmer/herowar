package controllers.api;

import static play.libs.Json.toJson;
import models.entity.game.Object3D;
import play.data.Form;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.mvc.Result;

/**
 * The Objects controller handle api requests for the Object3D model.
 * 
 * @author Sebastian Sachtleben
 */
public class Objects extends BaseAPI<Long, Object3D> {

  private Objects() {
    super(Long.class, Object3D.class);
  }

  public static final Objects instance = new Objects();

  public static Result list() {
    return instance.listAll();
  }

  public static Result show(Long id) {
    return instance.showEntry(id);
  }

  @Transactional
  public static Result update(Long id) {
    // Object3D object = instance.findUnique(id);
    Object3D object = JPA.em().merge(Form.form(Object3D.class).bindFromRequest().get());
    return ok(toJson(object));
  }

  public static Result delete(Long id) {
    return instance.deleteEntry(id);
  }

  public static Result add() {
    return instance.addEntry();
  }
}
