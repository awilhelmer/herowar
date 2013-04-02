package controllers.api;

import static play.libs.Json.toJson;
import models.entity.game.Object3D;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;


/**
 * The Objects controller handle api requests for the Object3D model.
 * 
 * @author Sebastian Sachtleben
 */
public class Objects extends Controller {

  public static Result list() {
    return ok(toJson(Object3D.getFinder().all()));
  }

  public static Result show(Long id) {
    Object3D object = Object3D.getFinder().where().eq("id", id).findUnique();
    return ok(toJson(object));
  }
  
  public static Result update(Long id) {
    Object3D object = Object3D.getFinder().where().eq("id", id).findUnique();
    Object3D.merge(object, Form.form(Object3D.class).bindFromRequest().get());
    return ok(toJson(object));
  }
  
  public static Result delete(Long id) {
    Object3D object = Object3D.getFinder().where().eq("id", id).findUnique();
    object.delete();
    return ok("{}");
  }

  public static Result add() {
    Object3D object = Form.form(Object3D.class).bindFromRequest().get();
    object.save();
    return ok(toJson(object));
  }

}
