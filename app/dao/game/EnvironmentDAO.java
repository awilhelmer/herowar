package dao.game;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import play.db.jpa.JPA;
import play.db.jpa.Transactional;

import models.entity.game.Environment;
import dao.BaseDAO;

public class EnvironmentDAO extends BaseDAO<Long, Environment> {

  private EnvironmentDAO() {
    super(Long.class, Environment.class);
  }

  private static final EnvironmentDAO instance = new EnvironmentDAO();

  @Transactional
  public static long getEnvironmentCount() {
    CriteriaBuilder builder = instance.getCriteriaBuilder();
    CriteriaQuery<Long> q = builder.createQuery(Long.class);
    Root<Environment> root = q.from(Environment.class);
    q.select(builder.count(root));
    return JPA.em().createQuery(q).getSingleResult();
  }
}
