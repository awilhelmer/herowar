package dao;

import java.io.Serializable;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import play.db.jpa.JPA;
import play.db.jpa.Transactional;

public abstract class BaseDAO<K extends Serializable, T extends Object> {
  @SuppressWarnings("unused")
  private Class<K> idClass;
  private Class<T> entityClass;

  public BaseDAO(Class<K> idClass, Class<T> entityClass) {
    this.idClass = idClass;
    this.entityClass = entityClass;
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
  

  @Transactional
  public boolean delete(K id) {
    try {
      T obj = JPA.em().find(entityClass, id);
      JPA.em().remove(obj);
    } catch (NoResultException e) {
      return false;
    }
    return true;
  }

  @Transactional
  public T findUnique(K id) {
    try {
      return JPA.em().find(entityClass, id);
    } catch (NoResultException e) {
      return null;
    }
  }

  @Transactional
  public T merge(T obj) {
    return JPA.em().merge(obj);
  }

}
