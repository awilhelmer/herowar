package game.processor.plugin;

import game.GameSession;
import game.network.server.PlayerStatsInitPacket;
import game.network.server.PlayerStatsUpdatePacket;
import game.processor.CacheConstants;
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

  private final static long SYNC_PERIOD = 30000;

  public GoldUpdatePlugin(GameProcessor processor) {
    super(processor);
  }

  @Override
  public void processSession(GameSession session, double delta, long now) {
    Date date = new Date();
    ConcurrentHashMap<String, Object> playerCache = getPlayerCache(session.getPlayerId());
    if (playerCache.containsKey(CacheConstants.GOLD)) {
      synchronized (playerCache) {
        // Update gold value
        if (getProcessor().isUpdateGold() && playerCache.containsKey(CacheConstants.GOLD_UPDATE)) {
          Long dif = date.getTime() - ((Date) playerCache.get(CacheConstants.GOLD_UPDATE)).getTime();
          double newGold = getGoldValue(playerCache) + (dif.doubleValue() / 1000 * getProcessor().getMap().getGoldPerTick());
          setPlayerCacheValue(playerCache, CacheConstants.GOLD, newGold);
        }
        // Update time update value
        setPlayerCacheValue(playerCache, CacheConstants.GOLD_UPDATE, date);
        // Send info to client
        if (!hashInitPacket(session.getPlayerId())) {
          sendPacket(session, new PlayerStatsInitPacket(getMap().getLives().longValue(), getRoundedGoldValue(playerCache), getMap().getGoldPerTick()));
          getInitPacket().replace(session.getPlayerId(), true);
          setPlayerCacheValue(playerCache, CacheConstants.GOLD_SYNC, date);
        } else {
          Long dif = date.getTime() - ((Date) playerCache.get(CacheConstants.GOLD_SYNC)).getTime();
          if (dif >= SYNC_PERIOD) {
            sendPacket(session, new PlayerStatsUpdatePacket((long) playerCache.get(CacheConstants.SCORE), getMap().getLives().longValue(),
                getRoundedGoldValue(playerCache), null, null, null));
            setPlayerCacheValue(playerCache, CacheConstants.GOLD_SYNC, date);
          }
        }
      }
    }
  }

  @Override
  public void addPlayer(GameSession session) {
    if (!getPlayerCache(session.getPlayerId()).containsKey(CacheConstants.GOLD)) {
      double startValue = getProcessor().getMap().getGoldStart().doubleValue();
      getPlayerCache(session.getPlayerId()).put(CacheConstants.GOLD, startValue);
    }
    getInitPacket().put(session.getPlayerId(), false);
  }

  @Override
  public void removePlayer(GameSession session) {
    // Do nothing, the gold should still updated when the player disconnects.
    // Maybe he returns after while...
  }

  @Override
  public String toString() {
    return "GoldUpdatePlugin";
  }

  private double getGoldValue(ConcurrentHashMap<String, Object> playerCache) {
    return Double.parseDouble(playerCache.get(CacheConstants.GOLD).toString());
  }

  private long getRoundedGoldValue(ConcurrentHashMap<String, Object> playerCache) {
    return Math.round(getGoldValue(playerCache));
  }
}
