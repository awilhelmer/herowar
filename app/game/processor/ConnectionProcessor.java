package game.processor;

import game.GameSession;
import game.Sessions;
import game.network.BasePacket;
import game.processor.meta.AbstractProcessor;

import org.codehaus.jackson.JsonNode;
import org.webbitserver.WebSocketConnection;

import play.libs.Json;

/**
 * Abstract processor to keep web socket connection and provides basic functionality.
 * 
 * @author Alexander Wilhelmer
 */
public abstract class ConnectionProcessor extends AbstractProcessor {

	protected WebSocketConnection connection;

	/**
	 * Default constructor.
	 * 
	 * @param String
	 *          The topic to set
	 * @param WebSocketConnection
	 *          The connection to set
	 */
	public ConnectionProcessor(String topic, WebSocketConnection connection) {
		super(topic);
		this.connection = connection;
	}

	/**
	 * Send a packet to the client.
	 * 
	 * @param Object
	 *          The packet to send
	 */
	public void sendPacket(BasePacket packet) {
		JsonNode node = Json.toJson(packet);
		connection.send(node.toString());
	}

	/**
	 * Get user session from games handler. If this returns null the client has already closed the connection.
	 * 
	 * @return GameSession
	 */
	public GameSession getSession() {
		return Sessions.get(connection);
	}

	public WebSocketConnection getConnection() {
		return connection;
	}

	public void setConnection(WebSocketConnection connection) {
		this.connection = connection;
	}
}
