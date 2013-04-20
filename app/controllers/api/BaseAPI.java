package controllers.api;

import static play.libs.Json.toJson;

import java.io.Serializable;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import play.data.Form;
import play.db.jpa.JPA;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * The BaseAPI provides the basic api functions.
 * 
 * @author Sebastian Sachtleben
 */
public abstract class BaseAPI<K extends Serializable, T extends Object> extends Controller {

  @SuppressWarnings("unused")
  private Class<K> idClass;
  private Class<T> entityClass;

  public BaseAPI(Class<K> idClass, Class<T> entityClass) {
    super();
    this.idClass = idClass;
    this.entityClass = entityClass;
  }

  protected Result listAll() {
    CriteriaQuery<T> crit = getCriteria();
    return ok(toJson(JPA.em().createQuery(crit).getResultList()));
  }

  protected CriteriaQuery<T> getCriteria() {
    return getCriteriaBuilder().createQuery(entityClass);
  }

  protected Root<T> getRoot() {
    return getCriteria().from(entityClass);
  }

  protected CriteriaBuilder getCriteriaBuilder() {
    return JPA.em().getCriteriaBuilder();
  }

  protected Result showEntry(K id) {
    try {
      return ok(toJson(JPA.em().find(entityClass, id)));
    } catch (NoResultException e) {
      return badRequest("No Result");
    }
  }

  public Result deleteEntry(K id) {
    try {
      T obj = JPA.em().find(entityClass, id);
      JPA.em().remove(obj);
    } catch (NoResultException e) {
      return badRequest("No Result");
    }
    return ok("{}");
  }

  public Result addEntry() {
    T obj = Form.form(entityClass).bindFromRequest().get();
    JPA.em().persist(obj);
    return ok(toJson(obj));
  }

  public T findUnique(K id) {
    try {
      return JPA.em().find(entityClass, id);
    } catch (NoResultException e) {
      return null;
    }
  }

  public T merge(T obj) {
    return JPA.em().merge(obj);
  }

}
