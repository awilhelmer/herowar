package game.network.client;

import game.network.ClientPacket;
import game.network.PacketType;
import game.network.server.ChatMessagePacket;
import game.network.server.ChatMessagePacket.Layout;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The ClientChatMessagePacket contains a message from a client and broadcast to the others.
 * 
 * @author Sebastian Sachtleben
 */
@ClientPacket(type = PacketType.ClientChatMessagePacket)
@SuppressWarnings("serial")
public class ClientChatMessagePacket extends BaseClientAuthPacket {
	private static final DateFormat df = new SimpleDateFormat("hh:mm");

	private String message;

	@Override
	public void process() {
		session.getGame().broadcast(
				new ChatMessagePacket(Layout.USER, "[" + df.format(new Date()) + "] " + session.getUsername() + ": " + message));
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
