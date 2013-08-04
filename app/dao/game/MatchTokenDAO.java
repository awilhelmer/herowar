package dao.game;

import java.util.List;

import models.entity.game.MatchToken;
import models.entity.game.Player;
import play.db.jpa.JPA;
import dao.BaseDAO;

public class MatchTokenDAO extends BaseDAO<String, MatchToken> {

	private MatchTokenDAO() {
		super(String.class, MatchToken.class);
	}

	private static final MatchTokenDAO instance = new MatchTokenDAO();

	public static MatchToken getTokenById(String token) {
		return instance.findUnique(token);
	}

	@SuppressWarnings("unchecked")
	public static MatchToken findValid(Player player) {
		List<MatchToken> tokens = JPA
				.em()
				.createQuery(
						"SELECT mt FROM " + MatchToken.class.getSimpleName() + " mt JOIN mt.player p WHERE p.id = :playerId AND mt.invalid = FALSE")
				.setParameter("playerId", player.getId()).getResultList();
		if (tokens.size() > 0) {
			return tokens.get(0);
		}
		return null;
	}

}
