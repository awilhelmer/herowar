package game.network.client;

import game.event.GameJoinEvent;
import game.network.BasePacket;
import game.network.InputPacket;
import game.network.handler.PacketHandler;
import game.network.handler.WebSocketHandler;
import game.network.server.AccessDeniedPacket;
import game.network.server.AccessGrantedPacket;
import models.entity.game.GameToken;

import org.bushe.swing.event.EventBus;
import org.webbitserver.WebSocketConnection;

import play.Logger;
import play.libs.Json;

/**
 * Initial client packet contains game token.
 * 
 * @author Alexander Wilhelmer
 */
public class ClientInitPacket extends BasePacket implements InputPacket {
  private static final long serialVersionUID = 993611144874360830L;
  private static final Logger.ALogger log = Logger.of(ClientInitPacket.class);

  private String token;

  public ClientInitPacket() {
    // empty
  }

  @Override
  public void process(PacketHandler packetHandler, WebSocketHandler socketHandler, WebSocketConnection connection) {
    GameToken gameToken = GameToken.getFinder().byId(token);
    if (gameToken != null) {
      socketHandler.getAuthConnections().put(connection, gameToken.getCreatedByUser());
      log.info("Found " + gameToken.toString());
      log.info("Auth connection " + connection.httpRequest().id() + " granted for " + gameToken.getCreatedByUser().toString());
      log.info("Total No. of subscribers: " + socketHandler.getAuthConnections().size() + ".");
      // TODO handle selected Models from Database
      EventBus.publish(new GameJoinEvent(gameToken, connection, "defaultmodel.dae"));
      connection.send(Json.toJson(new AccessGrantedPacket()).toString());
    } else {
      connection.send(Json.toJson(new AccessDeniedPacket()).toString());
    }
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((token == null) ? 0 : token.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ClientInitPacket other = (ClientInitPacket) obj;
    if (token == null) {
      if (other.token != null)
        return false;
    } else if (!token.equals(other.token))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "PlayerInitPacket [token=" + token + "]";
  }

}