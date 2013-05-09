package game.processor.plugin;

import game.GameSession;
import game.event.PreloadUpdateEvent;
import game.processor.GameProcessor;
import game.processor.meta.AbstractPlugin;
import game.processor.meta.IPlugin;

import java.util.concurrent.ConcurrentHashMap;

import org.bushe.swing.event.EventTopicSubscriber;

import play.Logger;

/**
 * The PreloadUpdatePlugin sends informations about the preload state of every connected player.
 * 
 * @author Sebastian Sachtleben
 */
public class PreloadUpdatePlugin extends AbstractPlugin implements IPlugin {
  
  private final static Logger.ALogger log = Logger.of(PreloadUpdatePlugin.class);
  
  private ConcurrentHashMap<Long, Integer> preloadProgress = new ConcurrentHashMap<Long, Integer>();
  private EventTopicSubscriber<PreloadUpdateEvent> preloadUpdateSubscriber = createPreloadUpdateSubscriber();
  
  public PreloadUpdatePlugin(GameProcessor processor) {
    super(processor);
    
  }

  @Override
  public void process() {
    // TODO Auto-generated method stub
  }
  
  @Override
  public void load() {
    getProcessor().addListener(preloadUpdateSubscriber);
  }

  @Override
  public void unload() {
    getProcessor().removeListener(preloadUpdateSubscriber);
  }

  @Override
  public void addPlayer(GameSession player) {
    long playerId = player.getUser().getId();
    if (!preloadProgress.containsKey(playerId)) {
      this.preloadProgress.put(playerId, 0);
    }
  }

  @Override
  public void removePlayer(GameSession player) {
    long playerId = player.getUser().getId();
    if (preloadProgress.containsKey(playerId)) {
      preloadProgress.remove(playerId);
    }
  }
  
  private EventTopicSubscriber<PreloadUpdateEvent> createPreloadUpdateSubscriber() {
    return new EventTopicSubscriber<PreloadUpdateEvent>() {
      @Override
      public void onEvent(String topic, PreloadUpdateEvent data) {
        if (preloadProgress.containsKey(data.getPlayerId())) {
          log.info("Update preload progress for user " + data.getPlayerId() + " with " + data.getProgress());
          preloadProgress.replace(data.getPlayerId(), data.getProgress());
        }
      }
    };
  }
  
  @Override
  public String toString() {
    return "PreloadUpdatePlugin";
  }
}
