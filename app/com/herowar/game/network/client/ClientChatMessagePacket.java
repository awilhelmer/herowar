package com.herowar.game.network.client;

import com.herowar.game.network.ClientPacket;
import com.herowar.game.network.PacketType;

/**
 * The ClientChatMessagePacket contains a message from a client and broadcast to the others.
 * 
 * @author Sebastian Sachtleben
 */
@ClientPacket(type = PacketType.ClientChatMessagePacket)
@SuppressWarnings("serial")
public class ClientChatMessagePacket extends BaseClientAuthPacket {

	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
