package game.network.client;

import game.Session;
import game.Sessions;
import game.network.BasePacket;
import game.network.InputPacket;
import game.network.handler.PacketHandler;
import game.network.handler.WebSocketHandler;

import org.webbitserver.WebSocketConnection;

import play.Logger;

/**
 * The ClientTutorialUpdatePacket will be send from client when he request to update the tutorial.
 * 
 * @author Sebastian Sachtleben
 */
@SuppressWarnings("serial")
public class ClientTutorialUpdatePacket extends BasePacket implements InputPacket {
	private static final Logger.ALogger log = Logger.of(ClientTutorialUpdatePacket.class);

	@Override
	public void process(PacketHandler packetHandler, WebSocketHandler socketHandler, WebSocketConnection connection) {
		Session session = Sessions.get(connection);
		if (session == null) {
			// TODO: disconnect user here ...
			log.error("GameSession should not be null");
			return;
		}
		session.getGame().setTutorialUpdate(true);
	}

	@Override
	public String toString() {
		return "ClientTutorialUpdatePacket [type=" + type + ", createdTime=" + createdTime + "]";
	}
}
