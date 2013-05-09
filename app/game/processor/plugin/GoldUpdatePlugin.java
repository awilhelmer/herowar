package game.processor.plugin;

import game.GameSession;
import game.network.server.PlayerStatusPacket;
import game.processor.GameProcessor;
import game.processor.meta.AbstractPlugin;
import game.processor.meta.IPlugin;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import play.libs.Json;

/**
 * The GoldUpdatePlugin increment the gold for every player on the map on every
 * tick.
 * 
 * @author Sebastian Sachtleben
 */
public class GoldUpdatePlugin extends AbstractPlugin implements IPlugin {

  private ConcurrentHashMap<Long, Long> goldCache = new ConcurrentHashMap<Long, Long>();

  public GoldUpdatePlugin(GameProcessor processor) {
    super(processor);
  }

  @Override
  public void process() {
    updateGold();
    sendStats();
  }

  @Override
  public void addPlayer(GameSession player) {
    long playerId = player.getUser().getId();
    if (!goldCache.containsKey(playerId)) {
      long startValue = getProcessor().getMap().getGoldStart().longValue();
      this.goldCache.put(playerId, startValue);
    }
  }

  @Override
  public void removePlayer(GameSession player) {
    // Do nothing, the gold should stay and still keep updated since the user
    // could rejoin...
  }

  private void updateGold() {
    Iterator<java.util.Map.Entry<Long, Long>> iter = goldCache.entrySet().iterator();
    while (iter.hasNext()) {
      java.util.Map.Entry<Long, Long> entry = iter.next();
      long newGold = entry.getValue() + getProcessor().getMap().getGoldPerTick();
      entry.setValue(newGold);
    }
  }

  private void sendStats() {
    for (GameSession session : getProcessor().getSessions()) {
      PlayerStatusPacket packet = new PlayerStatusPacket(getProcessor().getMap().getLives(), goldCache.get(session.getUser().getId()));
      session.getConnection().send(Json.toJson(packet).toString());
    }
  }

  @Override
  public String toString() {
    return "GoldUpdatePlugin";
  }
}
