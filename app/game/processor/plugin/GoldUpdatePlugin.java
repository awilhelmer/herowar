package game.processor.plugin;

import game.GameSession;
import game.network.BasePacket;
import game.network.server.PlayerStatsInitPacket;
import game.network.server.PlayerStatsUpdatePacket;
import game.processor.GameProcessor;
import game.processor.meta.AbstractPlugin;
import game.processor.meta.IPlugin;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import models.entity.game.Map;
import play.libs.Json;

/**
 * The GoldUpdatePlugin increment the gold for every player on the map on every
 * tick.
 * 
 * @author Sebastian Sachtleben
 */
public class GoldUpdatePlugin extends AbstractPlugin implements IPlugin {

  private ConcurrentHashMap<Long, Long> playerGold = new ConcurrentHashMap<Long, Long>();
  private ConcurrentHashMap<Long, Boolean> playerInit = new ConcurrentHashMap<Long, Boolean>();

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
    if (!playerGold.containsKey(playerId)) {
      long startValue = getProcessor().getMap().getGoldStart().longValue();
      playerGold.put(playerId, startValue);
      playerInit.put(playerId, false);
    }
  }

  @Override
  public void removePlayer(GameSession player) {
    long playerId = player.getUser().getId();
    playerInit.put(playerId, false);
  }

  private void updateGold() {
    Iterator<java.util.Map.Entry<Long, Long>> iter = playerGold.entrySet().iterator();
    while (iter.hasNext()) {
      java.util.Map.Entry<Long, Long> entry = iter.next();
      long newGold = entry.getValue() + getProcessor().getMap().getGoldPerTick();
      entry.setValue(newGold);
    }
  }

  private void sendStats() {
    for (GameSession session : getProcessor().getSessions()) {
      long playerId = session.getUser().getId();
      Map map = getProcessor().getMap();
      BasePacket packet = null;
      if (playerInit.get(playerId)) {
//        packet = new PlayerStatsUpdatePacket(map.getLives(), playerGold.get(playerId));
      } else {
        packet = new PlayerStatsInitPacket(map.getLives(), playerGold.get(playerId), map.getGoldPerTick());
        playerInit.replace(playerId, true);
        sendPacket(session, packet);
      }
    }
  }
  
  private void sendPacket(GameSession session, BasePacket packet) {
    session.getConnection().send(Json.toJson(packet).toString());
  }

  @Override
  public String toString() {
    return "GoldUpdatePlugin";
  }
}
