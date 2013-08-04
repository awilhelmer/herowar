package game.network.client;

import game.Session;
import game.Sessions;

import org.webbitserver.WebSocketConnection;

/**
 * Provides authorization safty for client packet.
 * <p>
 * If the connection is not authorized an {@link RuntimeException} occur and the packet processing stops.
 * </p>
 * 
 * @author Sebastian Sachtleben
 */
@SuppressWarnings("serial")
public abstract class BaseClientAuthPacket extends BaseClientPacket {

	protected Session session;

	/*
	 * (non-Javadoc)
	 * 
	 * @see game.network.client.BaseClientPacket#process(org.webbitserver.WebSocketConnection)
	 */
	@Override
	public void process(WebSocketConnection connection) {
		if (!Sessions.contains(connection)) {
			throw new RuntimeException("Connection not authorized...");
		}
		this.session = Sessions.get(connection);
		super.process(connection);
	}
}
