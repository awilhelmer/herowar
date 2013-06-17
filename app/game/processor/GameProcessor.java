package game.processor;

import game.GameClock;
import game.GameSession;
import game.event.GameStateEvent;
import game.models.TowerModel;
import game.models.UnitModel;
import game.network.BasePacket;
import game.network.server.PreloadDataPacket;
import game.network.server.TowerBuildPacket;
import game.network.server.TowerTargetPacket;
import game.network.server.UnitInPacket;
import game.processor.meta.AbstractProcessor;
import game.processor.meta.IPlugin;
import game.processor.meta.IProcessor;
import game.processor.plugin.FinishPlugin;
import game.processor.plugin.GoldUpdatePlugin;
import game.processor.plugin.PreloadUpdatePlugin;
import game.processor.plugin.TowerUpdatePlugin;
import game.processor.plugin.UnitUpdatePlugin;
import game.processor.plugin.WaveUpdatePlugin;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import models.entity.game.Map;
import models.entity.game.Match;
import models.entity.game.Vector3;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.RuntimeTopicEventSubscriber;
import org.webbitserver.WebSocketConnection;

import play.Logger;
import play.libs.Json;

import com.ardor3d.scenegraph.Node;

/**
 * The GameProcessor handles every game specific actions like updating player
 * gold, control waves, tower damage and much more.
 * 
 * @author Alexander Wilhelmer
 * @author Sebastian Sachtleben
 */
public class GameProcessor extends AbstractProcessor implements IProcessor {

  private final static Logger.ALogger log = Logger.of(GameProcessor.class);

  private long gameId;
  private long objectId = 0l;
  private Match match;
  private Map map;

  private GameClock clock = new GameClock();
  private PreloadDataPacket preloadPacket = null;

  private boolean wavesFinished = false;
  private boolean unitsFinished = false;
  private boolean waveRequest = false;
  private boolean updateGold = false;
  private State state;

  private final Node rootNode = new Node();

  /**
   * The session set contains all sessions for this game.
   */
  private Set<GameSession> sessions = Collections.synchronizedSet(new HashSet<GameSession>());

  /**
   * The plugins map contains all running plugins for each game state.
   */
  private java.util.Map<State, Set<IPlugin>> plugins = new HashMap<State, Set<IPlugin>>();

  private Set<UnitModel> units = Collections.synchronizedSet(new HashSet<UnitModel>());

  /**
   * The player cache contains player specific variables and properties like
   * gold and is used by plugins.
   */
  private ConcurrentHashMap<Long, ConcurrentHashMap<String, Object>> playerCache = new ConcurrentHashMap<Long, ConcurrentHashMap<String, Object>>();

  /**
   * The tower cache contains all placed towers on the map by any player.
   */
  private ConcurrentHashMap<Long, TowerModel> towerCache = new ConcurrentHashMap<Long, TowerModel>();

  public GameProcessor(Match match) {
    super("match-" + match.getId());
    this.gameId = match.getId();
    this.match = match;
    this.map = match.getMap();
    AnnotationProcessor.process(this);
    this.registerPlugins();
    this.updateState(State.PRELOAD);
    this.start();
  }

  @Override
  public void process() {
    double delta = clock.getDelta();
    long now = clock.getCurrentTime();
    try {
      Iterator<IPlugin> iter = plugins.get(state).iterator();
      while (iter.hasNext()) {
        IPlugin plugin = iter.next();
        plugin.process(delta, now);
      }
      checkGameState();
    } catch (Exception e) {
      log.error("Unexpected Error during game process occured:", e);
      log.error("Stopping " + this.toString());
      shutdown();
    }
  }

  @Override
  public int getUpdateTimer() {
    return 150;
  }

  @Override
  public void start() {
    log.debug(this.toString() + " started");
    super.start();
  }

  @Override
  public void stop() {
    log.debug(this.toString() + " stopped");
    AnnotationProcessor.unprocess(this);
    super.stop();
  }
  
  /**
   * Shutdown game processor.
   */
  private void shutdown() {
    Iterator<GameSession> iter = sessions.iterator();
    while(iter.hasNext()) {
      GameSession session = iter.next();
      session.getConnection().close();
    }
    stop();
  }

  /**
   * Add a new player to this game.
   * 
   * @param session
   *          The session to set
   */
  public void addPlayer(GameSession session) {
    synchronized (sessions) {
      sessions.add(session);
    }

    // Change session on tower if player reconnects
    synchronized (towerCache) {
      Iterator<TowerModel> iter = towerCache.values().iterator();
      while (iter.hasNext()) {
        TowerModel model = iter.next();
        if (model.getSession().getPlayerId() == session.getPlayerId()) {
          model.setSession(session);
        }
      }
    }

    // Create score and kills for player if not exists
    if (!playerCache.containsKey(session.getPlayerId())) {
      playerCache.put(session.getPlayerId(), new ConcurrentHashMap<String, Object>());
      playerCache.get(session.getPlayerId()).put(CacheConstants.SCORE, 0L);
      playerCache.get(session.getPlayerId()).put(CacheConstants.KILLS, 0L);
    }

    // Add the session to all plugins
    for (Set<IPlugin> statePlugins : plugins.values()) {
      for (IPlugin plugin : statePlugins) {
        plugin.addPlayer(session);
      }
    }
  }

  /**
   * Remove a player from this game.
   * 
   * @param connection
   *          The connection to set
   */
  public void removePlayer(WebSocketConnection connection) {
    GameSession player = null;
    synchronized (sessions) {
      Iterator<GameSession> iter = sessions.iterator();
      while (iter.hasNext()) {
        player = iter.next();
        if (player.getConnection().equals(connection)) {
          rootNode.detachChild(player.getModel());
          iter.remove();
          return;
        }
      }
    }
    for (Set<IPlugin> statePlugins : plugins.values()) {
      for (IPlugin plugin : statePlugins) {
        plugin.removePlayer(player);
      }
    }
  }

  /**
   * Broadcast a packet to all clients. Its possible to set if the trigger
   * connection should also retrieve the message.
   * 
   * @param connection
   *          The connection to set
   * @param message
   *          The message to set
   * @param sendSelf
   *          The sendSelf to set
   */
  public void broadcast(WebSocketConnection connection, BasePacket message, boolean sendSelf) {
    Iterator<GameSession> iter = sessions.iterator();
    while (iter.hasNext()) {
      GameSession session = iter.next();
      if (!session.isPreloading() && (sendSelf || !connection.equals(session.getConnection()))) {
        session.getConnection().send(Json.toJson(message).toString());
      }
    }
  }

  /**
   * Broadcast a packet to all clients.
   * 
   * @param message
   *          The message to set
   */
  public void broadcast(BasePacket message) {
    broadcast(null, message, true);
  }

  /**
   * Syncronize the player with the current game szenario.
   * 
   * @param session
   *          The session to set
   */
  public void syncronizePlayer(GameSession session) {
    log.info("Start sycronizing for player " + session.getPlayerId());
    Iterator<UnitModel> iter = session.getGame().getUnits().iterator();
    while (iter.hasNext()) {
      UnitModel unit = iter.next();
      log.info("Send player info about unit " + unit.getId() + " at " + unit.getTranslation().toString());
      session.getConnection().send(Json.toJson(new UnitInPacket(unit)).toString());
    }
    Iterator<TowerModel> iter2 = session.getGame().getTowerCache().values().iterator();
    while (iter2.hasNext()) {
      TowerModel tower = iter2.next();
      Vector3 position = new Vector3();
      position.setX(tower.getTranslation().getX());
      position.setY(tower.getTranslation().getY());
      position.setZ(tower.getTranslation().getZ());
      log.info("Send player info about tower " + tower.getId() + " at " + position.toString());
      session.getConnection().send(Json.toJson(new TowerBuildPacket(tower.getId(), tower.getDbId(), tower.getSession().getPlayerId(), position)).toString());
      if (tower.getTarget() != null) {
        log.info("Send player info about tower " + tower.getId() + " target unit " + tower.getTarget().getId());
        session.getConnection().send(Json.toJson(new TowerTargetPacket(tower.getId(), tower.getTarget().getId())).toString());
      }
    }
    log.info("Finish sycronizing for player " + session.getPlayerId());
  }

  private void registerPlugins() {
    for (State state : State.values()) {
      plugins.put(state, new HashSet<IPlugin>());
    }
    plugins.get(State.PRELOAD).add(new PreloadUpdatePlugin(this));
    plugins.get(State.GAME).add(new GoldUpdatePlugin(this));
    plugins.get(State.GAME).add(new TowerUpdatePlugin(this));
    plugins.get(State.GAME).add(new UnitUpdatePlugin(this));
    plugins.get(State.GAME).add(new WaveUpdatePlugin(this));
    plugins.get(State.FINISH).add(new FinishPlugin(this));
  }

  private void checkGameState() {
    if (getState().equals(State.GAME) && (getMap().getLives() <= 0 || (isWavesFinished() && isUnitsFinished()))) {
      updateState(State.FINISH);
    }
  }

  @RuntimeTopicEventSubscriber(methodName = "getStateTopic")
  public void updateStateByEvent(String topic, GameStateEvent event) {
    updateState(event.getState());
    clock.reset();
  }

  public String getStateTopic() {
    return getTopicName(Topic.STATE);
  }

  private void updateState(State state) {
    if (this.state != null) {
      for (IPlugin plugin : plugins.get(this.state)) {
        plugin.unload();
      }
    }
    if (state != null) {
      for (IPlugin plugin : plugins.get(state)) {
        plugin.load();
      }
    }
    this.state = state;
  }

  public void publish(Topic topic, Object obj) {
    EventBus.publish(getTopicName(topic), obj);
  }

  public String getTopicName(Topic topic) {
    return getTopicName() + "-" + topic.name().toLowerCase();
  }

  public enum Topic {
    STATE, PRELOAD, UNIT
  }

  @Override
  public String toString() {
    return "GameProcessor-" + gameId;
  }

  // GETTER && SETTER //

  public long getGameId() {
    return gameId;
  }

  public synchronized long getNextObjectId() {
    return objectId++;
  }

  public Set<GameSession> getSessions() {
    return sessions;
  }

  public java.util.Map<State, Set<IPlugin>> getPlugins() {
    return plugins;
  }

  public ConcurrentHashMap<Long, ConcurrentHashMap<String, Object>> getPlayerCache() {
    return playerCache;
  }

  public ConcurrentHashMap<Long, TowerModel> getTowerCache() {
    return towerCache;
  }

  public Match getMatch() {
    return match;
  }

  public Map getMap() {
    return map;
  }

  public State getState() {
    return state;
  }

  public void addUnit(UnitModel unit) {
    this.units.add(unit);
  }

  public Set<UnitModel> getUnits() {
    return units;
  }

  public boolean isWavesFinished() {
    return wavesFinished;
  }

  public void setWavesFinished(boolean wavesFinished) {
    this.wavesFinished = wavesFinished;
  }

  public boolean isUnitsFinished() {
    return unitsFinished;
  }

  public void setUnitsFinished(boolean unitsFinished) {
    this.unitsFinished = unitsFinished;
  }

  public boolean isWaveRequest() {
    return waveRequest;
  }

  public void setWaveRequest(boolean waveRequest) {
    this.waveRequest = waveRequest;
  }

  public boolean isUpdateGold() {
    return updateGold;
  }

  public void setUpdateGold(boolean updateGold) {
    this.updateGold = updateGold;
  }

  public PreloadDataPacket getPreloadPacket() {
    return preloadPacket;
  }

  public void setPreloadPacket(PreloadDataPacket preloadPacket) {
    this.preloadPacket = preloadPacket;
  }

  public enum State {
    PRELOAD, GAME, FINISH
  }
}
