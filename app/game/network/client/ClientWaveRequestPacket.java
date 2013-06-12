package game.network.client;

import game.network.BasePacket;
import game.network.InputPacket;
import game.network.handler.PacketHandler;
import game.network.handler.WebSocketHandler;

import org.webbitserver.WebSocketConnection;

import play.Logger;

/**
 * Send from client when and ask to send out the next wave.
 * 
 * @author Sebastian Sachtleben
 */
@SuppressWarnings("serial")
public class ClientWaveRequestPacket extends BasePacket implements InputPacket {
  private static final Logger.ALogger log = Logger.of(ClientWaveRequestPacket.class);

  @Override
  public void process(PacketHandler packetHandler, WebSocketHandler socketHandler, WebSocketConnection connection) {
    log.info("Request next wave...");
  }

}
