package game.processor.plugin;

import game.GameSession;
import game.network.server.GameDefeatPacket;
import game.network.server.GameVictoryPacket;
import game.processor.GameProcessor;
import game.processor.meta.AbstractPlugin;
import game.processor.meta.IPlugin;

import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import models.entity.User;
import models.entity.game.GameResult;
import models.entity.game.GameToken;
import play.db.jpa.JPA;
import dao.UserDAO;
import dao.game.GameTokenDAO;

/**
 * The FinishPlugin sends informations about the state of the game and clean up.
 * 
 * @author Sebastian Sachtleben
 */
public class FinishPlugin extends AbstractPlugin implements IPlugin {

  private long finishTimer = -1;
  private boolean done = false;

  public FinishPlugin(GameProcessor processor) {
    super(processor);
  }

  @Override
  public void process(double delta, long now) {
    if (finishTimer == -1) {
      finishTimer = now;
    }
    if (!done && finishTimer + 2000 <= now) {
      done = true;
      finishTimer = now;
      if (getMap().getLives() <= 0) {
        GameDefeatPacket packet = new GameDefeatPacket();
        broadcast(packet);
      } else {
        GameVictoryPacket packet = new GameVictoryPacket();
        broadcast(packet);
      }
      saveResults();
    } else if (done && finishTimer + 2000 <= now) {
      // TODO: stop properly the game here and cleanup
      getProcessor().stop();
    }
  }

  private void saveResults() {
    boolean victory = getMap().getLives() > 0;
    Iterator<GameSession> iter = getProcessor().getSessions().iterator();
    while (iter.hasNext()) {
      final GameSession session = iter.next();
      final GameResult result = new GameResult();
      result.setCdate(new Date());
      result.setLives(getMap().getLives());
      result.setMap(getMap());
      result.setVictory(victory);
      ConcurrentHashMap<String, Object> cache = getPlayerCache(session.getUser().getId());
      result.setScore(Math.round(Double.parseDouble(cache.get("score").toString())));
      JPA.withTransaction(new play.libs.F.Callback0() {
        @Override
        public void invoke() throws Throwable {
          GameToken token = GameTokenDAO.getTokenById(session.getToken().getToken());
          User user = UserDAO.getInstance().getById(session.getUser().getId());
          if (user != null && !token.getInvalid()) {
            result.setPlayer(user.getPlayer());
            result.setToken(token);
            JPA.em().persist(result);
            token.setResult(result);
            token.setInvalid(true);
          }
        }
      });
    }
  }

  @Override
  public void addPlayer(GameSession player) {
    // TODO Auto-generated method stub
  }

  @Override
  public void removePlayer(GameSession player) {
    // TODO Auto-generated method stub
  }
}
