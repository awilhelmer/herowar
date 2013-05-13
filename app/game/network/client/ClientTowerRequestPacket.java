package game.network.client;

import models.entity.game.Vector3;

import org.webbitserver.WebSocketConnection;

import play.Logger;

import game.network.BasePacket;
import game.network.InputPacket;
import game.network.handler.PacketHandler;
import game.network.handler.WebSocketHandler;

/**
 * The ClientTowerRequestPacket will be send from client when he request to build a tower somewhere.
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
	  log.info("Process " + this.toString());
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
