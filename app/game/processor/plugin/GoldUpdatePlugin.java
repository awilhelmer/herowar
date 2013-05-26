package game.processor.plugin;

import game.GameSession;
import game.network.server.PlayerStatsInitPacket;
import game.network.server.PlayerStatsUpdatePacket;
import game.processor.GameProcessor;
import game.processor.meta.IPlugin;
import game.processor.meta.UpdateSessionPlugin;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The GoldUpdatePlugin increment the gold for every player on the map on every
 * tick.
 * 
 * @author Sebastian Sachtleben
 */
public class GoldUpdatePlugin extends UpdateSessionPlugin implements IPlugin {

  private final static String SCORE_VALUE = "score";
  private final static String GOLD_VALUE = "gold";
  private final static String GOLD_UPDATE = "gold_update";
  private final static String GOLD_SYNC = "gold_sync";
  private final static long SYNC_PERIOD = 30000;

  public GoldUpdatePlugin(GameProcessor processor) {
    super(processor);
  }

  @Override
  public void processSession(GameSession session) {
    long playerId = session.getUser().getId();
    Date date = new Date();
    ConcurrentHashMap<String, Object> playerCache = getPlayerCache(playerId);
    if (playerCache.containsKey(GOLD_VALUE)) {
      synchronized (playerCache) {
      // Update gold value
        if (playerCache.containsKey(GOLD_UPDATE)) {
          Long dif = date.getTime() - ((Date) playerCache.get(GOLD_UPDATE)).getTime();
          double newGold = getGoldValue(playerCache) + (dif.doubleValue() / 1000 * getProcessor().getMap().getGoldPerTick());
          setPlayerCacheValue(playerCache, GOLD_VALUE, newGold);
        }
        // Update time update value
        setPlayerCacheValue(playerCache, GOLD_UPDATE, date);
        // Send info to client
        if (!hashInitPacket(playerId)) {
          sendPacket(session, new PlayerStatsInitPacket(getMap().getLives().longValue(), getRoundedGoldValue(playerCache), getMap().getGoldPerTick()));
          getInitPacket().replace(playerId, true);
          setPlayerCacheValue(playerCache, GOLD_SYNC, date);
        } else {
          Long dif = date.getTime() - ((Date) playerCache.get(GOLD_SYNC)).getTime();
          if (dif >= SYNC_PERIOD) {
            sendPacket(session, new PlayerStatsUpdatePacket((long) playerCache.get(SCORE_VALUE), getMap().getLives().longValue(), getRoundedGoldValue(playerCache), null, null, null));
            setPlayerCacheValue(playerCache, GOLD_SYNC, date);
          }
        }
      }
    }
  }

  @Override
  public void addPlayer(GameSession player) {
    long playerId = player.getUser().getId();
    if (!getPlayerCache(playerId).containsKey(GOLD_VALUE)) {
      double startValue = getProcessor().getMap().getGoldStart().doubleValue();
      getPlayerCache(playerId).put(GOLD_VALUE, startValue);
    }
    getInitPacket().put(playerId, false);
  }

  @Override
  public void removePlayer(GameSession player) {
    // Do nothing, the gold should still updated when the player disconnects.
    // Maybe he returns after while...
  }

  @Override
  public String toString() {
    return "GoldUpdatePlugin";
  }

  private double getGoldValue(ConcurrentHashMap<String, Object> playerCache) {
    return Double.parseDouble(playerCache.get(GOLD_VALUE).toString());
  }

  private long getRoundedGoldValue(ConcurrentHashMap<String, Object> playerCache) {
    return Math.round(getGoldValue(playerCache));
  }
}
