package game.network.client;

import game.GameSession;
import game.GamesHandler;
import game.models.TowerModel;
import game.network.BasePacket;
import game.network.InputPacket;
import game.network.handler.PacketHandler;
import game.network.handler.WebSocketHandler;
import game.network.server.ChatMessagePacket;
import game.network.server.ChatMessagePacket.Layout;
import game.network.server.PlayerStatsUpdatePacket;
import game.network.server.TowerBuildPacket;
import game.processor.CacheConstants;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import models.entity.game.Tower;
import models.entity.game.Vector3;

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
    if (currentGold < entity.getPrice()) {
      // TODO: not enough gold ...
      return;
    }
    TowerModel tower = new TowerModel(session.getGame().getNextObjectId(), entity);
    com.ardor3d.math.Vector3 position = new com.ardor3d.math.Vector3(this.position.getX(), 0, this.position.getZ());
    tower.setTranslation(position);
    tower.updateWorldTransform(false);
    tower.setSession(session);
    session.getGame().getTowerCache().put(tower.getId(), tower);
    session.getGame().broadcast(new TowerBuildPacket(tower.getId(), tower.getDbId(), session.getPlayer().getId(), this.position));
    DateFormat df = new SimpleDateFormat("hh:mm");
    session.getGame().broadcast(new ChatMessagePacket(Layout.SYSTEM, "[" + df.format(new Date()) + "] System: " + session.getUsername() + " build " + tower.getName()));
    synchronized (playerCache) {
      playerCache.replace(CacheConstants.GOLD, currentGold - entity.getPrice());
      playerCache.replace(CacheConstants.GOLD_SYNC, (new Date().getTime()));
    }
    session.getConnection().send(
        Json.toJson(new PlayerStatsUpdatePacket(null, null, Math.round(currentGold - entity.getPrice()), null, null, new Long(entity.getPrice() * -1)))
            .toString());
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
