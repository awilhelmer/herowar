package game.network;

import game.network.handler.PacketHandler;
import game.network.handler.WebSocketHandler;

import org.webbitserver.WebSocketConnection;

/**
 * Marked as input packet received from client.
 * 
 * @author Sebastian Sachtleben
 */

public interface InputPacket {

  public void process(PacketHandler packetHandler, WebSocketHandler socketHandler, WebSocketConnection connection);
  
}
