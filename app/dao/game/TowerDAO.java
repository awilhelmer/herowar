package dao.game;

import models.entity.game.Tower;
import dao.BaseDAO;

public class TowerDAO extends BaseDAO<Long, Tower> {

  private TowerDAO() {
    super(Long.class, Tower.class);
  }

  private static final TowerDAO instance = new TowerDAO();

  public static TowerDAO getInstance() {
    return instance;
  }
  
}
