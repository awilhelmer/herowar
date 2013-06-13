package game.processor.plugin;

import game.GameSession;
import game.event.GameStateEvent;
import game.event.PreloadUpdateEvent;
import game.network.server.GameStartPacket;
import game.processor.GameProcessor;
import game.processor.GameProcessor.State;
import game.processor.GameProcessor.Topic;
import game.processor.meta.AbstractPlugin;
import game.processor.meta.IPlugin;

import java.util.concurrent.ConcurrentHashMap;

import org.bushe.swing.event.annotation.RuntimeTopicEventSubscriber;

import play.Logger;

/**
 * The PreloadUpdatePlugin sends informations about the preload state of every
 * connected player.
 * 
 * @author Sebastian Sachtleben
 */
public class PreloadUpdatePlugin extends AbstractPlugin implements IPlugin {

  private final static Logger.ALogger log = Logger.of(PreloadUpdatePlugin.class);

  private ConcurrentHashMap<Long, Integer> preloadProgress = new ConcurrentHashMap<Long, Integer>();

  private Integer preloadPlayerMissing = 0;

  public PreloadUpdatePlugin(GameProcessor processor) {
    super(processor);
    preloadPlayerMissing = getMatch().getPlayerResults().size();
    log.info("Start preloading phase for " + preloadPlayerMissing + " players");
  }

  @Override
  public void process(double delta, long now) {
    if (preloadProgress.size() > 0 && preloadPlayerMissing == 0) {
      log.info("All player finshed preloading - switching game state to " + State.GAME);
      getProcessor().publish(Topic.STATE, new GameStateEvent(State.GAME));
      getProcessor().broadcast(new GameStartPacket());
    }
  }

  @Override
  public void addPlayer(GameSession session) {
    if (!preloadProgress.containsKey(session.getPlayerId())) {
      preloadProgress.put(session.getPlayerId(), 0);
    }
  }

  @Override
  public void removePlayer(GameSession session) {
    // Do nothing, the preload state should not change when a user disconnects.
    // Once the timeout will be reached the game should start automatically...
  }

  @RuntimeTopicEventSubscriber(methodName = "getPreloadTopic")
  public void onPreloadUpdateEvent(String topic, PreloadUpdateEvent data) {
    if (preloadProgress.containsKey(data.getPlayerId())) {
      log.info("Update preload progress for user " + data.getPlayerId() + " with " + data.getProgress());
      if (preloadProgress.get(data.getPlayerId()) == 100 && data.getProgress() < 100) {
        // Player finished preloaded before but seems reconnected and need
        // to load again
        log.info("Player " + data.getPlayerId() + " has reconnected");
        preloadPlayerMissing++;
      } else if (preloadProgress.get(data.getPlayerId()) < 100 && data.getProgress() == 100) {
        // Player fully preloaded and waiting for starting game
        log.info("Player " + data.getPlayerId() + " finished preloading");
        preloadPlayerMissing--;
      }
      preloadProgress.replace(data.getPlayerId(), data.getProgress());
    }
  }

  public String getPreloadTopic() {
    return getProcessor().getTopicName(Topic.PRELOAD);
  }

  @Override
  public String toString() {
    return "PreloadUpdatePlugin";
  }
}
