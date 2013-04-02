package controllers.api;

import static play.libs.Json.toJson;

import java.io.Serializable;

import play.db.ebean.Model.Finder;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * The BaseAPI provides the basic api functions.
 * 
 * @author Sebastian Sachtleben
 */
public abstract class BaseAPI<K extends Serializable, T extends Serializable> extends Controller {

  private K keyClass;
  private T entityClass;

  public static <K, T> Result list(Class<K> keyClass, Class<T> entityClass) {
    final Finder<K, T> finder = new Finder<K, T>(keyClass, entityClass);
    return ok(toJson(finder.all()));
  }
  
  // GETTER & SETTER //
  
  public K getKeyClass() {
    return keyClass;
  }

  public void setKeyClass(K keyClass) {
    this.keyClass = keyClass;
  }

  public T getEntityClass() {
    return entityClass;
  }

  public void setEntityClass(T entityClass) {
    this.entityClass = entityClass;
  }
}
