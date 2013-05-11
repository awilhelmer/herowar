package game.processor;

import game.GameClock;
import game.GameSession;
import game.event.GameStateEvent;
import game.models.UnitModel;
import game.network.BasePacket;
import game.processor.meta.AbstractProcessor;
import game.processor.meta.IPlugin;
import game.processor.meta.IProcessor;
import game.processor.plugin.GoldUpdatePlugin;
import game.processor.plugin.PreloadUpdatePlugin;
import game.processor.plugin.UnitUpdatePlugin;
import game.processor.plugin.WaveUpdatePlugin;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import models.entity.game.Map;
import models.entity.game.Unit;

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

  private final Node rootNode;
  private Long objectIdGenerator = null;
  private Long gameId;
  private GameClock clock;
  private Map map;
  private State state;

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

  public GameProcessor(Long gameId, Map map, GameSession session) {
    super("game-" + gameId);
    this.gameId = gameId;
    this.map = map;
    this.objectIdGenerator = 0l;
    this.rootNode = new Node();

    AnnotationProcessor.process(this);
    this.registerPlugins();
    this.updateState(State.PRELOAD);
    this.addPlayer(session);
  }

  @Override
  public void process() {
    if (clock == null) {
      clock = new GameClock();
    }
    Double delta = clock.getDelta();
    log.debug("Process " + getTopicName() + " with state " + state.toString() + " and players " + Arrays.toString(sessions.toArray()));
    for (IPlugin plugin : plugins.get(state)) {
      plugin.process(delta);
    }
  }

  @Override
  public int getUpdateTimer() {
    return 150;
  }

  @Override
  public void stop() {
    log.debug(this.toString() + " stopped");
    AnnotationProcessor.unprocess(this);
    super.stop();
  }

  public void addPlayer(GameSession player) {
    synchronized (sessions) {
      sessions.add(player);
    }
    long playerId = player.getUser().getId();
    if (!playerCache.containsKey(playerId)) {
      playerCache.put(playerId, new ConcurrentHashMap<String, Object>());
    }
    for (Set<IPlugin> statePlugins : plugins.values()) {
      for (IPlugin plugin : statePlugins) {
        plugin.addPlayer(player);
      }
    }
  }

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

  public void broadcast(WebSocketConnection connection, BasePacket message, boolean sendSelf) {
    for (GameSession session : sessions) {
      if (sendSelf || !connection.equals(session.getConnection())) {
        session.getConnection().send(Json.toJson(message).toString());
      }
    }
  }

  public void broadcast(BasePacket message) {
    broadcast(null, message, true);
  }

  private void registerPlugins() {
    for (State state : State.values()) {
      plugins.put(state, new HashSet<IPlugin>());
    }
    plugins.get(State.PRELOAD).add(new PreloadUpdatePlugin(this));
    plugins.get(State.GAME).add(new GoldUpdatePlugin(this));
    plugins.get(State.GAME).add(new WaveUpdatePlugin(this));
    plugins.get(State.GAME).add(new UnitUpdatePlugin(this));
  }

  @RuntimeTopicEventSubscriber(methodName = "getStateTopic")
  public void updateStateByEvent(String topic, GameStateEvent event) {
    updateState(event.getState());
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

  // GETTER && SETTER //

  public synchronized Long getObjectIdGenerator() {
    return new Long(objectIdGenerator++);
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

  public Long getGameId() {
    return gameId;
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

  public enum State {
    PRELOAD, GAME, FINISH
  }
}
