package game.network.client;

import game.network.ClientPacket;
import game.network.PacketType;

/**
 * Send from client when and ask to send out the next wave.
 * 
 * @author Sebastian Sachtleben
 */
@ClientPacket(type = PacketType.ClientWaveRequestPacket)
@SuppressWarnings("serial")
public class ClientWaveRequestPacket extends BaseClientAuthPacket {

	@Override
	public String toString() {
		return "ClientWaveRequestPacket [type=" + type + ", createdTime=" + createdTime + "]";
	}

}
