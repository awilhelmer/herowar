package game.network.server;

import game.network.BasePacket;
import game.network.PacketType;

/**
 * The ChatMessagePacket will be send to all sessions and contains a chat
 * message.
 * 
 * @author Sebastian Sachtleben
 */
@SuppressWarnings("serial")
public class ChatMessagePacket extends BasePacket {

  private String message;

  public ChatMessagePacket(String message) {
    super();
    this.type = PacketType.ChatMessagePacket;
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  @Override
  public String toString() {
    return "ChatMessagePacket [type=" + type + "]";
  }
}
