package game.processor;

import game.GameSession;
import game.network.BasePacket;
import game.processor.meta.AbstractProcessor;
import game.processor.meta.IProcessor;

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
import com.ardor3d.scenegraph.Spatial;

/**
 * @author Alexander Wilhelmer
 */
public class GameProcessor extends AbstractProcessor implements IProcessor {

  private final static Logger.ALogger log = Logger.of(GameProcessor.class);

  private final Node rootNode;
  private Long gameId;
  private Map map;

  private Set<GameSession> sessions = Collections.synchronizedSet(new HashSet<GameSession>());
  private Long objectIdGenerator = null;

  public GameProcessor(Long gameId, Map map, GameSession session) {
    super("game-" + gameId + "-map-" + map.getId());
    this.gameId = gameId;
    this.map = map;
    this.objectIdGenerator = 0l;
    this.addPlayer(session);
    this.rootNode = new Node();
    this.loadMap();
  }

  @Override
  public void process() {
    log.debug("Updating " + getTopic() + " with players " + Arrays.toString(sessions.toArray()));
    proccessObjects();
    processIntelligent();
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

  // public Set<SceneObject> getVisibleObjects(GameSession session) {
  // Set<SceneObject> objects = new HashSet<SceneObject>();
  //
  // for (SceneObject obj : this.objects) {
  // if (obj.getObject() != null) {
  // double distance = GameMath.distanceTo(session.getObject().getPosition(),
  // new Vector3(obj.getObject()
  // .getPosition().getX(), obj.getObject().getPosition().getY(),
  // obj.getObject().getPosition().getZ()));
  // log.debug("Distance " + Math.round(distance) + " from " +
  // session.getUser().getUsername() + " to "
  // + obj.toString());
  // if (!session.getVisibleObjects().contains(obj) && distance <
  // GameConfiguration.DEFAULT_VIEW_RANGE) {
  // log.debug("Object " + obj.toString() + " in range for " +
  // session.getUser().getUsername());
  // objects.add(obj);
  // }
  // }
  // }
  //
  // for (GameSession player : sessions) {
  // if (player.getObject() != null) {
  // if (!GameUtil.isSameSession(session, player)) {
  // double distance = GameMath.distanceTo(session.getObject().getPosition(),
  // player.getObject().getPosition());
  // log.debug("Distance " + Math.round(distance) + " from " +
  // session.getUser().getUsername() + " to "
  // + player.getUser().getUsername());
  // if (!session.getVisibleObjects().contains(player) && distance <
  // GameConfiguration.DEFAULT_VIEW_RANGE) {
  // log.debug("Player " + player.getUser().getUsername() + " in range for " +
  // session.getUser().getUsername());
  // objects.add(player);
  // }
  // }
  // }
  // }
  //
  // return objects;
  // }

  public void addPlayer(GameSession player) {
    synchronized (sessions) {
      this.sessions.add(player);
    }
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

  private void processIntelligent() {
    // TODO handle intelligent here
  }

  private void proccessObjects() {
    for (Spatial obj : rootNode.getChildren()) {
      // TODO add game logic here ...

    }
  }

  /**
   * TODO init the map Polygon-information ...
   */
  private void loadMap() {

  }

  public synchronized Long getObjectIdGenerator() {
    return new Long(objectIdGenerator++);
  }

  public Set<GameSession> getSessions() {
    return sessions;
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
