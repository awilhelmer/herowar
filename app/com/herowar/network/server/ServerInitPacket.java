package com.herowar.network.server;

import com.herowar.network.BasePacket;
import com.herowar.network.PacketType;
import com.herowar.network.client.ClientInitPacket;

/**
 * Notifies the client about running game web socket server and triggers the client to answer with {@link ClientInitPacket}.
 * 
 * @author Sebastian Sachtleben
 */
@SuppressWarnings("serial")
public class ServerInitPacket extends BasePacket {

	public ServerInitPacket() {
		super(PacketType.ServerInitPacket);
	}

	@Override
	public String toString() {
		return "ServerInitPacket [type=" + type + ", createdTime=" + createdTime + "]";
	}
}