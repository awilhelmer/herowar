package dao.game;

import models.entity.game.MatchToken;
import dao.BaseDAO;

public class MatchTokenDAO extends BaseDAO<String, MatchToken> {

  private MatchTokenDAO() {
    super(String.class, MatchToken.class);
  }

  private static final MatchTokenDAO instance = new MatchTokenDAO();

  public static MatchToken getTokenById(String token) {
    return instance.findUnique(token);
  }

}
