package game.network;

import game.network.handler.PacketHandler;

import org.webbitserver.WebSocketConnection;
import org.webbitserver.WebSocketHandler;

/**
 * Marked as input packet received from client.
 * 
 * @author Sebastian Sachtleben
 */

public interface InputPacket {

  public void process(PacketHandler packetHandler, WebSocketHandler socketHandler, WebSocketConnection connection);
  
}
