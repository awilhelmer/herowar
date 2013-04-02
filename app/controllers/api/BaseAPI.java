package controllers.api;

import static play.libs.Json.toJson;

import java.io.Serializable;

import models.entity.BaseModel;
import play.data.Form;
import play.db.ebean.Model.Finder;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * The BaseAPI provides the basic api functions.
 * 
 * @author Sebastian Sachtleben
 */
public abstract class BaseAPI<K extends Serializable, T extends BaseModel> extends Controller {

  @SuppressWarnings("unused")
  private Class<K> idClass;
  private Class<T> entityClass;
  private Finder<K, T> finder;
  
  public BaseAPI(Class<K> idClass, Class<T> entityClass) {
    super();
    this.finder = new Finder<K, T>(idClass, entityClass);
    this.idClass = idClass;
    this.entityClass = entityClass;
  }

  protected Result listAll() {
    return ok(toJson(getFinder().all()));
  }
  
  protected Result showEntry(K id) {
    return ok(toJson(getFinder().where().eq("id", id).findUnique()));
  }
  
  public Result deleteEntry(K id) {
    T obj = getFinder().where().eq("id", id).findUnique();
    obj.delete();
    return ok("{}");
  }

  public Result addEntry() {
    T obj = Form.form(entityClass).bindFromRequest().get();
    obj.save();
    return ok(toJson(obj));
  }
  
  public T findUnique(Long id) {
    return getFinder().where().eq("id", id).findUnique();
  }

  public Finder<K, T> getFinder() {
    return finder;
  }
}
