package game.network.client;

import org.webbitserver.WebSocketConnection;

import play.Logger;

import game.network.BasePacket;
import game.network.InputPacket;
import game.network.handler.PacketHandler;
import game.network.handler.WebSocketHandler;

/**
 * Send from client when preloading is done.
 * 
 * @author Sebastian Sachtleben
 */
@SuppressWarnings("serial")
public class ClientPreloadCompletePacket extends BasePacket implements InputPacket {

  private static final Logger.ALogger log = Logger.of(ClientPreloadCompletePacket.class);
  
  @Override
  public void process(PacketHandler packetHandler, WebSocketHandler socketHandler, WebSocketConnection connection) {
    // TODO Auto-generated method stub
  }

  @Override
  public String toString() {
    return "ClientPreloadCompletePacket [type=" + type + "]";
  }
}
