package game.network.client;

import game.network.ClientPacket;
import game.network.PacketType;
import play.Logger;

/**
 * Send from client when and ask to send out the next wave.
 * 
 * @author Sebastian Sachtleben
 */
@ClientPacket(type = PacketType.ClientWaveRequestPacket)
@SuppressWarnings("serial")
public class ClientWaveRequestPacket extends BaseClientAuthPacket {
	private static final Logger.ALogger log = Logger.of(ClientWaveRequestPacket.class);

	@Override
	public void process() {
		log.info("Request next wave...");
		session.getGame().setWaveRequest(true);
	}

}
