package dao.game;

import models.entity.game.Path;
import dao.BaseDAO;

public class PathDAO extends BaseDAO<Long, Path> {

  private PathDAO() {
    super(Long.class, Path.class);
  }

  private static final PathDAO instance = new PathDAO();

  
  public static PathDAO getInstance() {
    return instance;
  }

}
