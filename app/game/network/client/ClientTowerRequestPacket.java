package game.network.client;

import game.GameSession;
import game.GamesHandler;
import game.models.TowerModel;
import game.models.TowerRestriction;
import game.network.BasePacket;
import game.network.InputPacket;
import game.network.handler.PacketHandler;
import game.network.handler.WebSocketHandler;
import game.network.server.ChatMessagePacket;
import game.network.server.ChatMessagePacket.Layout;
import game.network.server.GlobalMessagePacket;
import game.network.server.PlayerStatsUpdatePacket;
import game.network.server.TowerBuildPacket;
import game.network.server.TowerBuildRejectedPacket;
import game.processor.CacheConstants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import models.entity.game.Tower;
import models.entity.game.Vector3;
import models.entity.game.Wave;
import models.entity.game.Waypoint;

import org.webbitserver.WebSocketConnection;

import play.Logger;
import play.db.jpa.JPA;
import play.libs.Json;

/**
 * The ClientTowerRequestPacket will be send from client when he request to
 * build a tower somewhere.
 * 
 * @author Sebastian Sachtleben
 */
@SuppressWarnings("serial")
public class ClientTowerRequestPacket extends BasePacket implements InputPacket {
  private static final Logger.ALogger log = Logger.of(ClientTowerRequestPacket.class);

  private Long id;
  private Vector3 position;

  @Override
  public void process(PacketHandler packetHandler, WebSocketHandler socketHandler, WebSocketConnection connection) {
    GameSession session = GamesHandler.getInstance().getConnections().get(connection);
    if (session == null) {
      // TODO: disconnect user here ...
      log.error("GameSession should not be null");
      return;
    }
    // TODO: validate and reduce player and and check if the tower has a valid
    // position.
    double currentGold = 0;
    ConcurrentHashMap<String, Object> playerCache = session.getGame().getPlayerCache().get(session.getPlayer().getId());
    if (playerCache.containsKey(CacheConstants.GOLD)) {
      currentGold = (double) playerCache.get(CacheConstants.GOLD);
    }
    Tower entity = JPA.em().find(Tower.class, id);
    if (entity == null) {
      return;
    }
    com.ardor3d.math.Vector3 position = new com.ardor3d.math.Vector3(this.position.getX(), 0, this.position.getZ());
    if (!hasEnoughGold(session, currentGold, entity.getPrice()) || !isPlaceAllowed(session, position)) {
      session.getConnection().send(Json.toJson(new TowerBuildRejectedPacket()).toString());
      return;
    }
    TowerModel tower = new TowerModel(session.getGame().getNextObjectId(), entity);
    tower.setTranslation(position);
    tower.updateWorldTransform(false);
    tower.setSession(session);
    session.getGame().getTowerCache().put(tower.getId(), tower);
    session.getGame().broadcast(new TowerBuildPacket(tower, this.position));
    String message = session.getUsername() + " build " + tower.getName();
    session.getGame().broadcast(new GlobalMessagePacket(message));
    DateFormat df = new SimpleDateFormat("hh:mm");
    session.getGame().broadcast(new ChatMessagePacket(Layout.SYSTEM, "[" + df.format(new Date()) + "] System: " + message));
    synchronized (playerCache) {
      playerCache.replace(CacheConstants.GOLD, currentGold - entity.getPrice());
      playerCache.replace(CacheConstants.GOLD_SYNC, (new Date().getTime()));
    }
    session.getConnection().send(
        Json.toJson(new PlayerStatsUpdatePacket(null, null, Math.round(currentGold - entity.getPrice()), null, null, entity.getPrice() * -1)).toString());
  }

  private boolean hasEnoughGold(GameSession session, double currentGold, int price) {
    if (currentGold < price) {
      session.getConnection().send(Json.toJson(new GlobalMessagePacket("Insufficient gold to build the tower")).toString());
      return false;
    }
    return true;
  }

  private boolean isPlaceAllowed(GameSession session, com.ardor3d.math.Vector3 position) {
    if (!checkRestrictions(session, position)) {
      return false;
    }
    if (!checkWaypoints(session, position)) {
      return false;
    }
    if (!checkTowers(session, position)) {
      return false;
    }
    return true;
  }

  private boolean checkRestrictions(GameSession session, com.ardor3d.math.Vector3 position) {
    Iterator<TowerRestriction> iter = session.getGame().getTowerRestrictions().iterator();
    while (iter.hasNext()) {
      TowerRestriction restriction = iter.next();
      if (position.distance(restriction.getPosition().getX(), restriction.getPosition().getY(), restriction.getPosition().getZ()) >= restriction.getRadius()) {
        session.getConnection().send(Json.toJson(new GlobalMessagePacket("Tower must be build in marked area")).toString());
        log.info("Tower build request denied - Distance to marked area is "
            + position.distance(restriction.getPosition().getX(), restriction.getPosition().getY(), restriction.getPosition().getZ()));
        return false;
      }
    }
    return true;
  }

  private boolean checkWaypoints(GameSession session, com.ardor3d.math.Vector3 position) {
    Iterator<Wave> iter = session.getGame().getMap().getWaves().iterator();
    while (iter.hasNext()) {
      Wave wave = iter.next();
      Iterator<Waypoint> iter2 = wave.getPath().getDbWaypoints().iterator();
      while (iter2.hasNext()) {
        Waypoint waypoint = iter2.next();
        if (waypoint.getPosition().getArdorVector().distance(position) < 50) {
          session.getConnection().send(Json.toJson(new GlobalMessagePacket("Tower cant be build next to enemy paths")).toString());
          log.info("Tower build request denied - Distance to waypoint " + waypoint.toString() + " is "
              + waypoint.getPosition().getArdorVector().distance(position));
          return false;
        }
      }
    }
    return true;
  }

  private boolean checkTowers(GameSession session, com.ardor3d.math.Vector3 position) {
    Iterator<TowerModel> iter = session.getGame().getTowerCache().values().iterator();
    while (iter.hasNext()) {
      TowerModel towerModel = iter.next();
      if (towerModel.getTranslation().distance(position) < 25) {
        session.getConnection().send(Json.toJson(new GlobalMessagePacket("Tower cant be build next to other towers")).toString());
        log.info("Tower build request denied - Distance to tower " + towerModel.toString() + " is " + towerModel.getTranslation().distance(position));
        return false;
      }
    }
    return true;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Vector3 getPosition() {
    return position;
  }

  public void setPosition(Vector3 position) {
    this.position = position;
  }

  @Override
  public String toString() {
    return "ClientTowerRequestPacket [type=" + type + ", createdTime=" + createdTime + ", id=" + id + ", position=" + position + "]";
  }
}
