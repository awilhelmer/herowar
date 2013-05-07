package controllers.api.game;

import static play.libs.Json.toJson;
import models.api.error.NotLoggedInError;
import models.entity.User;
import models.entity.game.GameToken;

import org.apache.commons.lang.RandomStringUtils;

import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import controllers.Application;

/**
 * The GameTokens controller handle api requests for the GameToken model.
 * 
 * @author Sebastian Sachtleben
 */
public class GameTokens extends Controller {
  
  @Transactional
  public static Result create() {
    User user = Application.getLocalUser();
    if (user == null) {
      return badRequest(toJson(new NotLoggedInError()));
    }
    GameToken gameToken = new GameToken();
    gameToken.setToken(RandomStringUtils.randomAlphanumeric(50));
    gameToken.setCreatedByUser(user);
    JPA.em().persist(gameToken);
    return ok(toJson(gameToken));
  }

}
