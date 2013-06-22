package game.processor.meta;

import game.GameSession;
import game.network.BasePacket;
import game.processor.GameProcessor;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import models.entity.game.Map;
import models.entity.game.Match;

import org.bushe.swing.event.annotation.AnnotationProcessor;

import play.libs.Json;

/**
 * The AbstractPlugin provides useful functions and wrapper methods for the game
 * processor used by any plugin.
 * 
 * @author Sebastian Sachtleben
 */
public abstract class AbstractPlugin {

  /**
   * The game processor where this plugin runs.
   */
  protected GameProcessor processor;

  /**
   * The initPacket map contains the information if a user already get the init
   * packet for this plugin.
   */
  private ConcurrentHashMap<Long, Boolean> initPacket = new ConcurrentHashMap<Long, Boolean>();

  /**
   * Default constructor to set GameProcessor for this plugin.
   * 
   * @param processor
   *          The game processor
   */
  public AbstractPlugin(GameProcessor processor) {
    this.processor = processor;
  }

  /**
   * Load plugin and activate event system for this plugn.
   */
  public void load() {
    AnnotationProcessor.process(this);
  }

  /**
   * Unload plugin and deactivate event system for this plugin.
   */
  public void unload() {
    AnnotationProcessor.unprocess(this);
  }

  /**
   * Get GameProcessor for this plugin.
   * 
   * @return The game processor
   */
  public GameProcessor getProcessor() {
    return processor;
  }

  /**
   * Get the initPacket map containing which player already retrieve their init
   * packet.
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
   * Send a packet to a specific game session.
   * 
   * @param session
   *          The game session.
   * @param packet
   *          The packet to send.
   */
  public void sendPacket(GameSession session, BasePacket packet) {
    session.getConnection().send(Json.toJson(packet).toString());
  }

  /**
   * Get all player sessions for GameProcessor.
   * 
   * @return The game sessions
   */
  public Set<GameSession> getSessions() {
    return getProcessor().getSessions();
  }

  /**
   * Get match from GameProcessor.
   * 
   * @return The match
   */
  public Match getMatch() {
    return getProcessor().getMatch();
  }

  /**
   * Get map from GameProcessor.
   * 
   * @return The map
   */
  public Map getMap() {
    return getProcessor().getMap();
  }

  /**
   * Broadcast a packet to all players.
   * 
   * @param packet
   *          The packet to broadcast.
   */
  public void broadcast(BasePacket packet) {
    getProcessor().broadcast(packet);
  }

  /**
   * Get topic name for the GameProcessor.
   * 
   * @return The topic name
   */
  public String getTopicName() {
    return getProcessor().getTopicName();
  }

  /**
   * Get the player cache from the GameProcessor.
   * 
   * @return The player cache
   */
  public ConcurrentHashMap<Long, ConcurrentHashMap<String, Object>> getPlayerCache() {
    return getProcessor().getPlayerCache();
  }

  /**
   * Get a specific player cache entry from the GameProcessor by player id.
   * 
   * @param playerId
   *          The player id
   * @return Specific player cache
   */
  public ConcurrentHashMap<String, Object> getPlayerCache(long playerId) {
    return getProcessor().getPlayerCache().get(playerId);
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
