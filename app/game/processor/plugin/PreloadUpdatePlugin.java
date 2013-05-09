package game.processor.plugin;

import game.GameSession;
import game.event.GameStateEvent;
import game.event.PreloadUpdateEvent;
import game.processor.GameProcessor;
import game.processor.GameProcessor.State;
import game.processor.GameProcessor.Topic;
import game.processor.meta.AbstractPlugin;
import game.processor.meta.IPlugin;

import java.util.concurrent.ConcurrentHashMap;

import org.bushe.swing.event.EventBus;
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
//  private EventTopicSubscriber<PreloadUpdateEvent> preloadUpdateSubscriber = createPreloadUpdateSubscriber();

  private Integer preloadPlayerMissing = 0;

  public PreloadUpdatePlugin(GameProcessor processor) {
    super(processor);
  }

  @Override
  public void process() {
    // TODO: This maybe cause the PreloadUpdatePlugin to start the game to
    // early. The first player connects and reach fast 100% before the other
    // players even connected and the map will start. We need to get somewhere
    // the full player list for this game before starting this preload stuff and
    // we also missing disconnected player and some kind of timeout before the
    // game starts even when the players are not connected.
    if (preloadProgress.size() > 0 && preloadPlayerMissing == 0) {
      log.info("All player finshed preloading - switching game state to " + State.GAME);
      EventBus.publish(getProcessor().getTopicName(Topic.STATE), new GameStateEvent(State.GAME));
    }
  }

  @Override
  public void addPlayer(GameSession player) {
    long playerId = player.getUser().getId();
    if (!preloadProgress.containsKey(playerId)) {
      preloadPlayerMissing++;
      preloadProgress.put(playerId, 0);
    }
  }

  @Override
  public void removePlayer(GameSession player) {
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
        preloadPlayerMissing++;
      } else if (preloadProgress.get(data.getPlayerId()) < 100 && data.getProgress() == 100) {
        // Player fully preloaded and waiting for starting game
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
