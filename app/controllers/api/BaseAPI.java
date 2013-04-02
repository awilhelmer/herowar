package controllers.api;

import static play.libs.Json.toJson;

import java.io.Serializable;

import models.entity.BaseModel;
import play.db.ebean.Model.Finder;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * The BaseAPI provides the basic api functions.
 * 
 * @author Sebastian Sachtleben
 */
public class BaseAPI<T extends BaseModel, K extends Serializable> extends Controller {

  private Class<T> classEntity;
  private Class<K> classKey;

  public BaseAPI(Class<T> classEntity, Class<K> classKey) {
    super();
    this.classEntity = classEntity;
    this.classKey = classKey;
  }

  @SuppressWarnings("unchecked")
  protected Result listAll() {
    final Finder<K, T> finder = new Finder<K, T>(classKey, classEntity);
    return ok(toJson(finder.all()));
  }

  // GETTER & SETTER //

}
