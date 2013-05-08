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
    this.addPlayer(session);
    this.rootNode = new Node();
    this.registerPlugins();
  }

  @Override
  public void process() {
    log.debug("Process " + getTopic() + " with players " + Arrays.toString(sessions.toArray()));
    for (IPlugin plugin : plugins) {
      log.debug("Run plugin: " + plugin.toString());
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

  public void addPlayer(GameSession player) {
    synchronized (sessions) {
      this.sessions.add(player);
    }
//    this.playerGold.put(player.getUser().getId(), map.getGoldStart().longValue());
  }

  public void removePlayer(WebSocketConnection connection) {
    synchronized (sessions) {
      Iterator<GameSession> iter = sessions.iterator();
      while (iter.hasNext()) {
        GameSession player = iter.next();
        if (player.getConnection().equals(connection)) {
          rootNode.detachChild(player.getModel());
          iter.remove();
          return;
        }
      }
    }
  }
  
  private void registerPlugins() {
    plugins.add(new GoldUpdatePlugin());
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

  public void setGameId(Long gameId) {
    this.gameId = gameId;
  }

  public Map getMap() {
    return map;
  }

  public void setMap(Map map) {
    this.map = map;
  }
}
