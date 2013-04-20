package dao.game;

import models.entity.game.Environment;
import play.db.jpa.Transactional;
import dao.BaseDAO;

public class EnvironmentDAO extends BaseDAO<Long, Environment> {

  private EnvironmentDAO() {
    super(Long.class, Environment.class);
  }

  private static final EnvironmentDAO instance = new EnvironmentDAO();

  @Transactional
  public static long getEnvironmentCount() {
    return instance.getBaseCount();
  }
}
