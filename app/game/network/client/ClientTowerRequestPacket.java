package game.network.client;

import game.GameSession;
import game.GamesHandler;
import game.network.BasePacket;
import game.network.InputPacket;
import game.network.handler.PacketHandler;
import game.network.handler.WebSocketHandler;
import game.network.server.TowerBuildPacket;
import game.processor.models.Tower;
import models.entity.game.Vector3;

import org.webbitserver.WebSocketConnection;

import play.Logger;

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
    Tower tower = new Tower();
    tower.setObjectId(session.getGame().getObjectIdGenerator());
    tower.setTowerId(id);
    tower.setPostion(position);
    tower.setSession(session);
    session.getGame().getTowerCache().put(tower.getObjectId(), tower);
    session.getGame().broadcast(new TowerBuildPacket(tower.getObjectId(), tower.getTowerId(), session.getUser().getId(), tower.getPostion()));
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
