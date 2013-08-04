package game.network;

import org.webbitserver.WebSocketConnection;

/**
 * Marked as input packet received from client.
 * 
 * @author Sebastian Sachtleben
 */

public interface InputPacket {

	public void process(WebSocketConnection connection);

}
