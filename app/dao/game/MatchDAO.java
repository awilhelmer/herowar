package dao.game;

import java.util.List;

import models.entity.game.Match;
import models.entity.game.MatchState;
import play.db.jpa.JPA;
import dao.BaseDAO;

public class MatchDAO extends BaseDAO<String, Match> {

	private MatchDAO() {
		super(String.class, Match.class);
	}

	private static final MatchDAO instance = new MatchDAO();

	public static MatchDAO getInstance() {
		return instance;
	}

	@SuppressWarnings("unchecked")
	public Match getOpenMatch() {
		List<Match> matches = JPA.em()
				.createQuery("SELECT m FROM " + Match.class.getSimpleName() + " m WHERE m.state = :state ORDER BY m.cdate DESC")
				.setParameter("state", MatchState.INIT).setMaxResults(1).getResultList();
		if (matches.size() > 0) {
			return matches.get(0);
		}
		return null;
	}

}
