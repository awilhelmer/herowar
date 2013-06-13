package controllers.api.game;

import static play.libs.Json.toJson;
import game.json.excludes.GameResultExcludeMapDataMixin;

import java.io.IOException;

import models.api.error.NotLoggedInError;
import models.entity.User;
import models.entity.game.Map;
import models.entity.game.Match;
import models.entity.game.MatchResult;
import models.entity.game.MatchState;
import models.entity.game.MatchToken;

import org.apache.commons.lang.RandomStringUtils;
import org.codehaus.jackson.map.ObjectMapper;

import play.Logger;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import controllers.Application;
import dao.game.MapDAO;
import dao.game.MatchDAO;

/**
 * The Matches controller handle api requests for the Match model.
 * 
 * @author Sebastian Sachtleben
 */
public class Matches extends Controller {
  private static final Logger.ALogger log = Logger.of(Matches.class);

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

    Match match = new Match();
    match.setMap(MapDAO.getMapById(mapId));
    JPA.em().persist(match);

    ObjectMapper mapper = new ObjectMapper();
    mapper.getSerializationConfig().addMixInAnnotations(Map.class, GameResultExcludeMapDataMixin.class);
    try {
      return ok(mapper.writeValueAsString(match));
    } catch (IOException e) {
      log.error("Failed parse match to json:", e);
    }
    return badRequest();
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
    token.setResult(result);

    return ok(toJson(token));
  }
}
