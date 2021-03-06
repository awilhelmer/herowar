package com.herowar.controllers;

import static play.libs.Json.toJson;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


import org.apache.commons.lang.RandomStringUtils;
import org.codehaus.jackson.map.ObjectMapper;

import com.herowar.dao.MapDAO;
import com.herowar.dao.MatchDAO;
import com.herowar.dao.MatchResultDAO;
import com.herowar.dao.MatchTokenDAO;
import com.herowar.json.excludes.MatchExcludeMapDataMixin;
import com.herowar.json.excludes.MatchResultSimpleMixin;
import com.herowar.json.excludes.PlayerWithUsernameMixin;
import com.herowar.models.NotLoggedInError;
import com.herowar.models.entity.User;
import com.herowar.models.entity.game.Map;
import com.herowar.models.entity.game.Match;
import com.herowar.models.entity.game.MatchResult;
import com.herowar.models.entity.game.MatchState;
import com.herowar.models.entity.game.MatchToken;
import com.herowar.models.entity.game.Player;

import play.Logger;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.mvc.Result;

/**
 * The Matches controller handle api requests for the Match model.
 * 
 * @author Sebastian Sachtleben
 */
public class Matches extends BaseAPI<Long, Match> {
	private static final Logger.ALogger log = Logger.of(Matches.class);

	private Matches() {
		super(Long.class, Match.class);
	}

	public static final Matches instance = new Matches();

	@Transactional
	public static Result find() {
		User user = Application.getLocalUser();
		if (user == null) {
			return badRequest(toJson(new NotLoggedInError()));
		}
		MatchToken token = MatchTokenDAO.findValid(user.getPlayer());
		if (token != null) {
			return ok(toJson(token));
		}
		return ok("{}");
	}

	@Transactional
	public static Result show(Long id) {
		User user = Application.getLocalUser();
		if (user == null) {
			return badRequest(toJson(new NotLoggedInError()));
		}
		Match match = MatchDAO.getInstance().getById(id);
		ObjectMapper mapper = new ObjectMapper();
		mapper.getSerializationConfig().addMixInAnnotations(Player.class, PlayerWithUsernameMixin.class);
		mapper.getSerializationConfig().addMixInAnnotations(Map.class, MatchExcludeMapDataMixin.class);
		try {
			return ok(mapper.writeValueAsString(match));
		} catch (IOException e) {
			log.error("Failed parse match to json:", e);
		}
		return badRequest();
	}

	/**
	 * Create a new match for the given map id and returns the map as json.
	 * 
	 * @param mapId
	 *          The mapId to set.
	 * @return The match as json.
	 */
	@Transactional
	public static Result create(Long mapId) {
		User user = Application.getLocalUser();
		if (user == null) {
			return badRequest(toJson(new NotLoggedInError()));
		}

		Map map = MapDAO.getMapById(mapId);
		if (map == null) {
			return badRequest();
		}

		Match match = new Match();
		match.setMap(map);
		match.setLives(map.getLives());
		JPA.em().persist(match);

		ObjectMapper mapper = new ObjectMapper();
		mapper.getSerializationConfig().addMixInAnnotations(Map.class, MatchExcludeMapDataMixin.class);
		try {
			return ok(mapper.writeValueAsString(match));
		} catch (IOException e) {
			log.error("Failed parse match to json:", e);
		}
		return badRequest();
	}

	@Transactional
	public static Result start(Long id) {
		User user = Application.getLocalUser();
		if (user == null) {
			return badRequest(toJson(new NotLoggedInError()));
		}
		Match match = MatchDAO.getInstance().getById(id);
		match.setState(MatchState.PRELOAD);
		return ok(toJson(match));
	}

	/**
	 * Join a match with given id.
	 * 
	 * @param id
	 *          The id to set.
	 * @return The token as json.
	 */
	@Transactional
	public static Result join(Long id) {
		User user = Application.getLocalUser();
		if (user == null) {
			return badRequest(toJson(new NotLoggedInError()));
		}
		Match match = MatchDAO.getInstance().getById(id);
		if (match == null || !MatchState.INIT.equals(match.getState())) {
			log.error("User " + user.getUsername() + " tried to join not existing match or a match without INIT state.");
			return badRequest();
		}

		MatchToken token = new MatchToken();
		token.setToken(RandomStringUtils.randomAlphanumeric(50));
		token.setPlayer(user.getPlayer());
		JPA.em().persist(token);

		MatchResult result = new MatchResult();
		result.setPlayer(user.getPlayer());
		result.setToken(token);
		result.setMatch(match);
		JPA.em().persist(result);
		match.getPlayerResults().add(result);
		match.setUdate(new Date());
		token.setResult(result);

		return ok(toJson(token));
	}

	@Transactional
	public static Result joinMatch() {
		User user = Application.getLocalUser();
		if (user == null) {
			return badRequest(toJson(new NotLoggedInError()));
		}
		Match match = MatchDAO.getInstance().getOpenMatch();
		if (match != null) {
			return ok(toJson(match));
		}
		return badRequest();
	}

	@Transactional
	public static Result quit() {
		User user = Application.getLocalUser();
		if (user == null) {
			return badRequest(toJson(new NotLoggedInError()));
		}
		List<MatchResult> matchResults = MatchResultDAO.findOpen(user.getPlayer());
		Iterator<MatchResult> iter = matchResults.iterator();
		while (iter.hasNext()) {
			MatchResult result = iter.next();
			Match match = result.getMatch();
			MatchToken token = result.getToken();
			match.getPlayerResults().remove(result);
			token.setPlayer(null);
			token.setResult(null);
			result.setMatch(null);
			result.setPlayer(null);
			result.setToken(null);
			JPA.em().remove(result);
			JPA.em().remove(token);
			if (match.getPlayerResults().size() == 0) {
				match.setMap(null);
				JPA.em().remove(match);
			}
		}
		return ok();
	}

	/**
	 * Get the history of current logged in player.
	 * 
	 * @return The game history
	 */
	@Transactional
	public static Result history() {
		User user = Application.getLocalUser();
		if (user == null) {
			return badRequest(toJson(new NotLoggedInError()));
		}
		List<MatchResult> results = MatchResultDAO.getHistory(user.getPlayer());
		ObjectMapper mapper = new ObjectMapper();
		mapper.getSerializationConfig().addMixInAnnotations(MatchResult.class, MatchResultSimpleMixin.class);
		try {
			return ok(mapper.writeValueAsString(results));
		} catch (IOException e) {
			log.error("Failed to create history json:", e);
		}
		return badRequest();
	}
}
