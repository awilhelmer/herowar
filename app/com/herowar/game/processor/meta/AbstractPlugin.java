package com.herowar.game.processor.meta;


import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.herowar.game.network.BasePacket;
import com.herowar.game.network.Connection;
import com.herowar.game.processor.GameProcessor;
import com.herowar.models.entity.game.Map;
import com.herowar.models.entity.game.Match;


/**
 * The AbstractPlugin provides useful functions and wrapper methods for the game processor used by any plugin.
 * 
 * @author Sebastian Sachtleben
 */
public abstract class AbstractPlugin {

	/**
	 * The game processor where this plugin runs.
	 */
	protected GameProcessor game;

	/**
	 * The initPacket map contains the information if a user already get the init packet for this plugin.
	 */
	private ConcurrentHashMap<Long, Boolean> initPacket = new ConcurrentHashMap<Long, Boolean>();

	/**
	 * Default constructor to set GameProcessor for this plugin.
	 * 
	 * @param game
	 *          The game processor
	 */
	public AbstractPlugin(GameProcessor game) {
		this.game = game;
	}

	/**
	 * Load plugin.
	 */
	public void load() {
		// empty
	}

	/**
	 * Unload plugin.
	 */
	public void unload() {
		// empty
	}

	/**
	 * Get GameProcessor for this plugin.
	 * 
	 * @return The game processor
	 */
	public GameProcessor game() {
		return game;
	}

	/**
	 * Get the initPacket map containing which player already retrieve their init packet.
	 * 
	 * @return The init packet map
	 */
	public ConcurrentHashMap<Long, Boolean> getInitPacket() {
		return initPacket;
	}

	/**
	 * Get boolean if the player already retrieve his init packet.
	 * 
	 * @param playerId
	 *          The player id.
	 * @return boolean
	 */
	public Boolean hashInitPacket(long playerId) {
		return initPacket.get(playerId);
	}

	/**
	 * Get all player sessions for GameProcessor.
	 * 
	 * @return The game sessions
	 */
	public Set<Connection> connections() {
		return game().getConnections();
	}

	/**
	 * Get match from GameProcessor.
	 * 
	 * @return The match
	 */
	public Match getMatch() {
		return game().getMatch();
	}

	/**
	 * Get map from GameProcessor.
	 * 
	 * @return The map
	 */
	public Map getMap() {
		return game().getMap();
	}

	/**
	 * Broadcast a packet to all players.
	 * 
	 * @param packet
	 *          The packet to broadcast.
	 */
	public void broadcast(BasePacket packet) {
		game().broadcast(packet);
	}

	/**
	 * Get topic name for the GameProcessor.
	 * 
	 * @return The topic name
	 */
	public String getTopicName() {
		return game().getTopicName();
	}

	/**
	 * Get the player cache from the GameProcessor.
	 * 
	 * @return The player cache
	 */
	public ConcurrentHashMap<Long, ConcurrentHashMap<String, Object>> getPlayerCache() {
		return game().getPlayerCache();
	}

	/**
	 * Get a specific player cache entry from the GameProcessor by player id.
	 * 
	 * @param playerId
	 *          The player id
	 * @return Specific player cache
	 */
	public ConcurrentHashMap<String, Object> getPlayerCache(long playerId) {
		return game().getPlayerCache().get(playerId);
	}

	/**
	 * Set a specific value for a key in player cache for a player id.
	 * 
	 * @param playerId
	 *          The player id.
	 * @param key
	 *          The player cache key.
	 * @param value
	 *          The new player cache value.
	 */
	public void setPlayerCacheValue(long playerId, String key, Object value) {
		ConcurrentHashMap<String, Object> playerCache = getPlayerCache(playerId);
		setPlayerCacheValue(playerCache, key, value);
	}

	/**
	 * Set a specific value for a key of given player cache.
	 * 
	 * @param playerCache
	 *          The player cache.
	 * @param key
	 *          The player cache key.
	 * @param value
	 *          The new player cache value.
	 */
	public void setPlayerCacheValue(ConcurrentHashMap<String, Object> playerCache, String key, Object value) {
		if (playerCache.containsKey(key)) {
			playerCache.replace(key, value);
		} else {
			playerCache.put(key, value);
		}
	}
}
