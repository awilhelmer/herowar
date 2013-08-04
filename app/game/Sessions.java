package game;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.webbitserver.WebSocketConnection;

/**
 * Handles all game sessions.
 * 
 * @author Sebastian Sachtleben
 */
public class Sessions {

	/**
	 * Contains all {@link WebSocketConnection} connections and the matching {@link GameSession}.
	 */
	private ConcurrentHashMap<WebSocketConnection, GameSession> connections = new ConcurrentHashMap<WebSocketConnection, GameSession>();

	/**
	 * Keep private instance of {@link Sessions}.
	 */
	private static Sessions instance = new Sessions();

	/**
	 * Returns {@link Sessions} instance.
	 * 
	 * @return The {@link Sessions} instance.
	 */
	private static Sessions getInstance() {
		return instance;
	}

	/**
	 * Private constructor to prevent class others from creating {@link Sessions} instance.
	 */
	private Sessions() {
		// empty
	}

	/**
	 * Get {@link GameSession} for given {@link WebSocketConnection}.
	 * 
	 * @param connection
	 *          The {@link WebSocketConnection} connection.
	 * @return The matching {@link GameSession}.
	 */
	public static GameSession get(final WebSocketConnection connection) {
		return getInstance().getConnections().get(connection);
	}

	/**
	 * Checks if given {@link WebSocketConnection} exists.
	 * 
	 * @param connection
	 *          The {@link WebSocketConnection} connection.
	 * @return The success boolean.
	 */
	public static boolean contains(final WebSocketConnection connection) {
		return getInstance().getConnections().containsKey(connection);
	}

	/**
	 * Adds new {@link WebSocketConnection} and {@link GameSession}.
	 * 
	 * @param connection
	 *          The {@link WebSocketConnection} connection.
	 * @return The previous associated {@link GameSession}.
	 */
	public static GameSession add(final WebSocketConnection connection, final GameSession session) {
		return getInstance().getConnections().put(connection, session);
	}

	/**
	 * Removes the {@link GameSession} for given {@link WebSocketConnection}.
	 * 
	 * @param connection
	 *          The {@link WebSocketConnection} connection.
	 * @return The removed {@link GameSession}.
	 */
	public static GameSession remove(final WebSocketConnection connection) {
		return getInstance().getConnections().remove(connection);
	}

	/**
	 * Removes all sessions.
	 */
	public static void clear() {
		getInstance().getConnections().clear();
	}

	/**
	 * @return the connections
	 */
	public Map<WebSocketConnection, GameSession> getConnections() {
		return connections;
	}

}
