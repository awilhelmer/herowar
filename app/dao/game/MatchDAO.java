package dao.game;

import models.entity.game.Match;
import dao.BaseDAO;

public class MatchDAO extends BaseDAO<String, Match> {

  private MatchDAO() {
    super(String.class, Match.class);
  }

  private static final MatchDAO instance = new MatchDAO();

  public static MatchDAO getInstance() {
    return instance;
  }

}
