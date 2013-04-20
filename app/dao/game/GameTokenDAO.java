package dao.game;

import play.db.jpa.Transactional;
import models.entity.game.GameToken;
import dao.BaseDAO;

public class GameTokenDAO extends BaseDAO<String, GameToken> {

  private GameTokenDAO() {
    super(String.class, GameToken.class);
  }

  private static final GameTokenDAO instance = new GameTokenDAO();

  @Transactional
  public static GameToken getTokenById(String token) {
    return instance.findUnique(token);
  }

}
