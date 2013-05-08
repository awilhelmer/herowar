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

  private ConcurrentHashMap<Long, Long> playerGold = new ConcurrentHashMap<Long, Long>();

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
    this.playerGold.put(player.getUser().getId(), getProcessor().getMap().getGoldStart().longValue());
  }

  @Override
  public void removePlayer(GameSession player) {
    // Do nothing, the gold should stay and still keep updated since the user
    // could rejoin...
  }

  private void updateGold() {
    Iterator<java.util.Map.Entry<Long, Long>> iter = playerGold.entrySet().iterator();
    while (iter.hasNext()) {
      java.util.Map.Entry<Long, Long> entry = iter.next();
      entry.setValue(entry.getValue() + getProcessor().getMap().getGoldPerTick());
    }
  }

  private void sendStats() {
    for (GameSession session : getProcessor().getSessions()) {
      PlayerStatusPacket packet = new PlayerStatusPacket(getProcessor().getMap().getLives(), playerGold.get(session.getUser().getId()));
      session.getConnection().send(Json.toJson(packet).toString());
    }
  }

  @Override
  public String toString() {
    return "GoldUpdatePlugin";
  }
}
