package game.processor.plugin;

import game.processor.meta.AbstractPlugin;
import game.processor.meta.IPlugin;

import java.util.concurrent.ConcurrentHashMap;

/**
 * The GoldUpdatePlugin increment the gold for every player on the map on every tick.
 * 
 * @author Sebastian Sachtleben
 */
public class GoldUpdatePlugin extends AbstractPlugin implements IPlugin {

  private ConcurrentHashMap<Long, Long> playerGold = new ConcurrentHashMap<Long, Long>();
  
  @Override
  public void process() {
//    updateGold();
//    sendStats();
  }
  
//  private void updateGold() {
//    Iterator<java.util.Map.Entry<Long, Long>> iter = playerGold.entrySet().iterator();
//    while(iter.hasNext()) {
//      java.util.Map.Entry<Long, Long> entry = iter.next();
//      entry.setValue(entry.getValue() + map.getGoldPerTick());
//    }
//  }
  
//  private void sendStats() {
//    for (GameSession session : sessions) {
//      PlayerStatusPacket packet = new PlayerStatusPacket(map.getLives(), playerGold.get(session.getUser().getId()));
//      session.getConnection().send(Json.toJson(packet).toString());
//    }
//  }

  @Override
  public String toString() {
    return "GoldUpdatePlugin";
  }
}
