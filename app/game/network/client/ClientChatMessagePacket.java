package game.network.client;

import game.GameSession;
import game.GamesHandler;
import game.network.BasePacket;
import game.network.InputPacket;
import game.network.handler.PacketHandler;
import game.network.handler.WebSocketHandler;
import game.network.server.ChatMessagePacket;

import org.webbitserver.WebSocketConnection;

/**
 * The ClientChatMessagePacket contains a message from a client and broadcast to
 * the others.
 * 
 * @author Sebastian Sachtleben
 */
@SuppressWarnings("serial")
public class ClientChatMessagePacket extends BasePacket implements InputPacket {

  private String message;

  @Override
  public void process(PacketHandler packetHandler, WebSocketHandler socketHandler, WebSocketConnection connection) {
    GameSession session = GamesHandler.getInstance().getConnections().get(connection);
    session.getGame().broadcast(new ChatMessagePacket(message));
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
