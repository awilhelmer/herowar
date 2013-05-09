package game.processor;

import game.GameSession;
import game.network.BasePacket;
import game.processor.meta.AbstractProcessor;
import game.processor.meta.IPlugin;
import game.processor.meta.IProcessor;
import game.processor.plugin.GoldUpdatePlugin;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import models.entity.game.Map;

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
  private Map map;

  private Set<GameSession> sessions = Collections.synchronizedSet(new HashSet<GameSession>());
  private Set<IPlugin> plugins = Collections.synchronizedSet(new HashSet<IPlugin>());

  public GameProcessor(Long gameId, Map map, GameSession session) {
    super("game-" + gameId + "-map-" + map.getId());
    this.gameId = gameId;
    this.map = map;
    this.objectIdGenerator = 0l;
    this.rootNode = new Node();
    this.registerPlugins();
    this.addPlayer(session);
  }

  @Override
  public void process() {
    log.debug("Process " + getTopic() + " with players " + Arrays.toString(sessions.toArray()));
    for (IPlugin plugin : plugins) {
      plugin.process();
    }
  }

  @Override
  public int getUpdateTimer() {
    return 150;
  }

  @Override
  public void stop() {
    log.debug(this.toString() + " stopped");
    super.stop();
  }

  public void addPlayer(GameSession player) {
    synchronized (sessions) {
      this.sessions.add(player);
    }
    for (IPlugin plugin : plugins) {
      plugin.addPlayer(player);
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
    for (IPlugin plugin : plugins) {
      plugin.removePlayer(player);
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
    plugins.add(new GoldUpdatePlugin(this));
  }

  // GETTER && SETTER //

  public synchronized Long getObjectIdGenerator() {
    return new Long(objectIdGenerator++);
  }

  public Set<GameSession> getSessions() {
    return sessions;
  }

  public Set<IPlugin> getPlugins() {
    return plugins;
  }

  public Long getGameId() {
    return gameId;
  }

  public Map getMap() {
    return map;
  }
}
