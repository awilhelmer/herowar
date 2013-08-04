package game.network.server;

import game.network.BasePacket;
import game.network.PacketType;

/**
 * The ChatMessagePacket will be send to all sessions and contains a chat message.
 * 
 * @author Sebastian Sachtleben
 */
@SuppressWarnings("serial")
public class ChatMessagePacket extends BasePacket {

	private Layout layout;
	private String message;

	public ChatMessagePacket(Layout layout, String message) {
		super();
		this.type = PacketType.ChatMessagePacket;
		this.layout = layout;
		this.message = message;
	}

	public Layout getLayout() {
		return layout;
	}

	public void setLayout(Layout layout) {
		this.layout = layout;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public enum Layout {
		SYSTEM, USER
	}
}
