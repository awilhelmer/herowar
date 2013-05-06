package dao.game;

import models.entity.game.Wave;
import dao.BaseDAO;

public class WaveDAO extends BaseDAO<Long, Wave> {

  private WaveDAO() {
    super(Long.class, Wave.class);
  }

  private static final WaveDAO instance = new WaveDAO();

  
  public static WaveDAO getInstance() {
    return instance;
  }

}
