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
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.webbitserver.WebSocketConnection;

import play.Logger;
import play.libs.Json;
import dao.game.GameTokenDAO;

/**
 * Initial client packet contains game token.
 * 
 * @author Alexander Wilhelmer
 * @author Sebastian Sachtleben
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressWarnings("serial")
public class ClientInitPacket extends BasePacket implements InputPacket {
  
  @JsonIgnore
  private static final Logger.ALogger log = Logger.of(ClientInitPacket.class);

  @JsonProperty
  private String token;

  @Override
  public void process(PacketHandler packetHandler, WebSocketHandler socketHandler, WebSocketConnection connection) {
    GameToken gameToken = GameTokenDAO.getTokenById(token);
    if (gameToken != null) {
      socketHandler.getAuthConnections().put(connection, gameToken.getCreatedByUser());
      log.info("Found " + gameToken.toString());
      log.info("Auth connection " + connection.httpRequest().id() + " granted for " + gameToken.getCreatedByUser().toString());
      log.info("Total No. of subscribers: " + socketHandler.getAuthConnections().size() + ".");
      EventBus.publish(new GameJoinEvent(gameToken, connection));
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
