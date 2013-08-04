package game.network.client;

import game.network.BasePacket;
import game.network.InputPacket;

import org.webbitserver.WebSocketConnection;

/**
 * Provides basic functionality for client packets.
 * 
 * @author Sebastian Sachtleben
 */
@SuppressWarnings("serial")
public abstract class BaseClientPacket extends BasePacket implements InputPacket {

	protected WebSocketConnection connection;

	/*
	 * (non-Javadoc)
	 * 
	 * @see game.network.InputPacket#process(org.webbitserver.WebSocketConnection)
	 */
	@Override
	public void process(WebSocketConnection connection) {
		this.connection = connection;
		process();
	}

	/**
	 * Process the client packet.
	 */
	public abstract void process();
}
