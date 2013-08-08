package com.herowar.dao.game;

import java.util.List;

import com.herowar.dao.BaseDAO;
import com.herowar.models.entity.game.MatchResult;
import com.herowar.models.entity.game.MatchState;
import com.herowar.models.entity.game.Player;

import play.db.jpa.JPA;

public class MatchResultDAO extends BaseDAO<String, MatchResult> {

	private MatchResultDAO() {
		super(String.class, MatchResult.class);
	}

	private static final MatchResultDAO instance = new MatchResultDAO();

	public static MatchResultDAO getInstance() {
		return instance;
	}

	@SuppressWarnings("unchecked")
	public static List<MatchResult> getHistory(Player player) {
		return JPA
				.em()
				.createQuery(
						"SELECT mr FROM " + MatchResult.class.getSimpleName()
								+ " mr join mr.player p join mr.match m WHERE p.id = :playerId AND m.state = :state ORDER BY m.cdate DESC")
				.setParameter("playerId", player.getId()).setParameter("state", MatchState.FINISH).setMaxResults(10).getResultList();
	}

	@SuppressWarnings("unchecked")
	public static List<MatchResult> findOpen(Player player) {
		return JPA
				.em()
				.createQuery(
						"SELECT mr FROM " + MatchResult.class.getSimpleName()
								+ " mr join mr.player p join mr.match m WHERE p.id = :playerId AND m.state = :state ORDER BY m.cdate DESC")
				.setParameter("playerId", player.getId()).setParameter("state", MatchState.INIT).getResultList();
	}

}
