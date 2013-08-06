package game.network.client;

import game.network.ClientPacket;
import game.network.PacketType;

/**
 * The ClientTutorialUpdatePacket will be send from client when he request to update the tutorial.
 * 
 * @author Sebastian Sachtleben
 */
@ClientPacket(type = PacketType.ClientTutorialUpdatePacket)
@SuppressWarnings("serial")
public class ClientTutorialUpdatePacket extends BaseClientAuthPacket {

	@Override
	public String toString() {
		return "ClientTutorialUpdatePacket [type=" + type + ", createdTime=" + createdTime + "]";
	}

}
