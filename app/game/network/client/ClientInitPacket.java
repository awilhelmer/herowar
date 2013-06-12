package game.network.client;

import game.event.GameJoinEvent;
import game.network.BasePacket;
import game.network.InputPacket;
import game.network.handler.PacketHandler;
import game.network.handler.WebSocketHandler;
import game.network.server.AccessDeniedPacket;
import game.network.server.AccessGrantedPacket;
import models.entity.game.Map;
import models.entity.game.MatchToken;
import models.entity.game.Wave;

import org.bushe.swing.event.EventBus;
import org.hibernate.Hibernate;
import org.webbitserver.WebSocketConnection;

import play.Logger;
import play.libs.Json;
import dao.game.MatchTokenDAO;

/**
 * Initial client packet contains game token.
 * 
 * @author Alexander Wilhelmer
 * @author Sebastian Sachtleben
 */
@SuppressWarnings("serial")
public class ClientInitPacket extends BasePacket implements InputPacket {

  private static final Logger.ALogger log = Logger.of(ClientInitPacket.class);

  private String token;

  @Override
  public void process(PacketHandler packetHandler, WebSocketHandler socketHandler, WebSocketConnection connection) {
    MatchToken matchToken = MatchTokenDAO.getTokenById(token);
    if (matchToken != null) {
      socketHandler.getAuthConnections().put(connection, matchToken.getPlayer());
      log.info("Found " + matchToken.toString());
      log.info("Auth connection " + connection.httpRequest().id() + " granted for " + matchToken.getPlayer().toString());
      log.info("Total No. of subscribers: " + socketHandler.getAuthConnections().size() + ".");
      // TODO: Load waves since the map is detached after but a optimized query
      // should be faster then this?!?
      Map map = matchToken.getResult().getMatch().getMap();
      Hibernate.initialize(map.getWaves());
      for (Wave wave : map.getWaves()) {
        Hibernate.initialize(wave.getPath().getDbWaypoints());
        Hibernate.initialize(wave.getUnits());
        Hibernate.initialize(wave.getPath());
      }
      EventBus.publish(new GameJoinEvent(matchToken, connection));
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
    return "ClientInitPacket [type=" + type + ", createdTime=" + createdTime + ", token=" + token + "]";
  }
}
