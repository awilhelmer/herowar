package game.network.client;

import play.Logger;

/**
 * The ClientTutorialUpdatePacket will be send from client when he request to update the tutorial.
 * 
 * @author Sebastian Sachtleben
 */
@SuppressWarnings("serial")
public class ClientTutorialUpdatePacket extends BaseClientAuthPacket {
	private static final Logger.ALogger log = Logger.of(ClientTutorialUpdatePacket.class);

	@Override
	public void process() {
		log.info("Request next tutorial step...");
		session.getGame().setTutorialUpdate(true);
	}

	@Override
	public String toString() {
		return "ClientTutorialUpdatePacket [type=" + type + ", createdTime=" + createdTime + "]";
	}
}
