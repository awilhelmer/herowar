package game.network.server;

import game.network.BasePacket;
import game.network.PacketType;
import game.network.client.ClientInitPacket;

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