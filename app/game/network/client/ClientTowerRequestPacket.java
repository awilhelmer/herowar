package game.network.client;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import game.GameSession;
import game.GamesHandler;
import game.models.TowerModel;
import game.network.BasePacket;
import game.network.InputPacket;
import game.network.handler.PacketHandler;
import game.network.handler.WebSocketHandler;
import game.network.server.PlayerStatsUpdatePacket;
import game.network.server.TowerBuildPacket;
import models.entity.game.Vector3;

import org.webbitserver.WebSocketConnection;

import play.Logger;
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

  private final static String GOLD_VALUE = "gold";
  private final static String GOLD_SYNC = "gold_sync";
  
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
    ConcurrentHashMap<String, Object> playerCache = session.getGame().getPlayerCache().get(session.getUser().getId());
    if (playerCache.containsKey(GOLD_VALUE)) {
      currentGold = (double) playerCache.get(GOLD_VALUE);
    }
    int towerPrice = 500; // TODO: this should be real price...
    if (currentGold < towerPrice) {
      // TODO: not enough gold ...
      return;
    }
    TowerModel tower = new TowerModel(session.getGame().getObjectIdGenerator(), id);
    com.ardor3d.math.Vector3 position = new com.ardor3d.math.Vector3(this.position.getX(), 0, this.position.getZ());
    tower.setTranslation(position);
    tower.updateWorldTransform(false);
    tower.setSession(session);
    session.getGame().getTowerCache().put(tower.getId(), tower);
    session.getGame().broadcast(new TowerBuildPacket(tower.getId(), tower.getDbId(), session.getUser().getId(), this.position));
    playerCache.replace(GOLD_VALUE, currentGold - towerPrice);
    session.getConnection().send(Json.toJson(new PlayerStatsUpdatePacket(session.getGame().getMap().getLives(), Math.round(currentGold - towerPrice))).toString());
    playerCache.replace(GOLD_SYNC, new Date());
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
