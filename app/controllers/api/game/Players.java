package controllers.api.game;

import models.entity.game.Player;
import play.db.jpa.Transactional;
import play.mvc.Result;
import controllers.api.BaseAPI;

/**
 * The Players controller handle api requests for the Player model.
 * 
 * @author Sebastian Sachtleben
 */
public class Players extends BaseAPI<Long, Player> {

  private Players() {
    super(Long.class, Player.class);
  }

  public static final Players instance = new Players();

  @Transactional
  public static Result show(Long id) {
    return instance.showEntry(id);
  }
}