package com.herowar.game.processor.plugin;


import java.util.concurrent.ConcurrentHashMap;

import com.herowar.game.network.Connection;
import com.herowar.game.network.server.PlayerStatsInitPacket;
import com.herowar.game.network.server.PlayerStatsUpdatePacket;
import com.herowar.game.processor.CacheConstants;
import com.herowar.game.processor.GameProcessor;
import com.herowar.game.processor.GameProcessor.State;
import com.herowar.game.processor.meta.IPlugin;
import com.herowar.game.processor.meta.UpdateSessionPlugin;

/**
 * The GoldUpdatePlugin increment the gold for every player on the map on every tick.
 * 
 * @author Sebastian Sachtleben
 */
public class GoldPlugin extends UpdateSessionPlugin implements IPlugin {

	private final static long SYNC_PERIOD = 30000;

	public GoldPlugin(GameProcessor processor) {
		super(processor);
	}

	@Override
	public void processConnection(Connection connection, double delta, long now) {
		if (connection.preloading()) {
			return;
		}
		ConcurrentHashMap<String, Object> playerCache = getPlayerCache(connection.id());
		if (playerCache.containsKey(CacheConstants.GOLD)) {
			synchronized (playerCache) {
				// Update gold value
				if (game().isUpdateGold() && playerCache.containsKey(CacheConstants.GOLD_UPDATE)) {
					Long dif = now - Long.parseLong(playerCache.get(CacheConstants.GOLD_UPDATE).toString());
					double newGold = getGoldValue(playerCache) + (dif.doubleValue() / 1000 * game().getMap().getGoldPerTick());
					setPlayerCacheValue(playerCache, CacheConstants.GOLD, newGold);
				}
				// Update time update value
				setPlayerCacheValue(playerCache, CacheConstants.GOLD_UPDATE, now);
				// Send info to client
				if (!hashInitPacket(connection.id())) {
					connection.send(new PlayerStatsInitPacket(getMap().getLives().longValue(), getRoundedGoldValue(playerCache), getMap()
							.getGoldPerTick()));
					getInitPacket().replace(connection.id(), true);
					setPlayerCacheValue(playerCache, CacheConstants.GOLD_SYNC, now);
				} else {
					Long dif = now - Long.parseLong(playerCache.get(CacheConstants.GOLD_SYNC).toString());
					if (dif >= SYNC_PERIOD) {
						connection.send(new PlayerStatsUpdatePacket((long) playerCache.get(CacheConstants.SCORE), getMap().getLives().longValue(),
								getRoundedGoldValue(playerCache), null, null, null));
						setPlayerCacheValue(playerCache, CacheConstants.GOLD_SYNC, now);
					}
				}
			}
		}
	}

	@Override
	public void add(Connection connection) {
		if (!getPlayerCache(connection.id()).containsKey(CacheConstants.GOLD)) {
			double startValue = game().getMap().getGoldStart().doubleValue();
			if (getMatch().getPlayerResults().size() > 1) {
				startValue = Math.round(startValue / new Double(getMatch().getPlayerResults().size()) * 1.2);
			}
			getPlayerCache(connection.id()).put(CacheConstants.GOLD, startValue);
		}
		getInitPacket().put(connection.id(), false);
	}

	@Override
	public void remove(Connection connection) {
		// Do nothing, the gold should still updated when the player disconnects.
		// Maybe he returns after while...
	}

	private double getGoldValue(ConcurrentHashMap<String, Object> playerCache) {
		return Double.parseDouble(playerCache.get(CacheConstants.GOLD).toString());
	}

	private long getRoundedGoldValue(ConcurrentHashMap<String, Object> playerCache) {
		return Math.round(getGoldValue(playerCache));
	}

	@Override
	public State onState() {
		return State.GAME;
	}

	@Override
	public String toString() {
		return "GoldPlugin";
	}
}
