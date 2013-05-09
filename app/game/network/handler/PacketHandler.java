package game.network.handler;

import game.GamesHandler;
import game.network.BasePacket;
import game.network.InputPacket;
import game.network.PacketType;
import game.network.client.ClientInitPacket;
import game.network.client.ClientPreloadUpdatePacket;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.webbitserver.WebSocketConnection;

import play.Logger;
import play.db.jpa.JPA;
import play.libs.Json;

/**
 * @author Sebastian Sachtleben
 */
public class PacketHandler implements Serializable {

  private static final long serialVersionUID = 8096194902543478468L;
  private static PacketHandler instance = new PacketHandler();
  private static final Logger.ALogger log = Logger.of(GamesHandler.class);

  private Map<Integer, Class<? extends BasePacket>> packetTypeCache = new HashMap<Integer, Class<? extends BasePacket>>();

  private PacketHandler() {
    registerTypes();
  }

  public static PacketHandler getInstance() {
    return instance;
  }

  public void handle(final WebSocketHandler handler, final WebSocketConnection connection, String data) {
    BasePacket packetType = Json.fromJson(Json.parse(data), BasePacket.class);
    if (packetType == null || !getPacketTypeCache().containsKey(packetType.getType())) {
      log.error("Failed to get packet for type: " + (packetType != null ? packetType.getType() : null));
      return;
    }
    try {
      final InputPacket packet = (InputPacket) Json.fromJson(Json.parse(data), getPacketTypeCache().get(packetType.getType()));
      if (packet != null) {
        log.debug("Retrieved (connection " + connection.httpRequest().id() + "):" + packet.toString());
        JPA.withTransaction(new play.libs.F.Callback0() {
          @Override
          public void invoke() throws Throwable {
            packet.process(instance, handler, connection);
          }
        });
      }
    } catch (Exception e) {
      log.error("Failed to parse input packet (connection=" + connection.httpRequest().id() + "): " + data, e);
    }
  }

  private void registerTypes() {
    packetTypeCache.put(PacketType.ClientInitPacket, ClientInitPacket.class);
    packetTypeCache.put(PacketType.ClientPreloadUpdatePacket, ClientPreloadUpdatePacket.class);
  }

  public Map<Integer, Class<? extends BasePacket>> getPacketTypeCache() {
    if (packetTypeCache.size() == 0) {
      registerTypes();
    }
    return packetTypeCache;
  }

}
