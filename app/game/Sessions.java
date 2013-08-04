package game;

import org.webbitserver.WebSocketConnection;

/**
 * Handles all game sessions.
 * 
 * @author Sebastian Sachtleben
 */
public class Sessions extends Cache<WebSocketConnection, Session> {

	/**
	 * Keep private instance of {@link Sessions}.
	 */
	private static Sessions instance = new Sessions();

	/**
	 * Returns {@link Sessions} instance.
	 * 
	 * @return The {@link Sessions} instance.
	 */
	private static Sessions instance() {
		return instance;
	}

	/**
	 * Private constructor to prevent class others from creating {@link Sessions} instance.
	 */
	private Sessions() {
		// empty
	}

	/**
	 * Get {@link Session} for given {@link WebSocketConnection}.
	 * 
	 * @param connection
	 *          The {@link WebSocketConnection} connection.
	 * @return The matching {@link Session}.
	 */
	public static Session get(final WebSocketConnection connection) {
		return instance().cache().get(connection);
	}

	/**
	 * Checks if given {@link WebSocketConnection} exists.
	 * 
	 * @param connection
	 *          The {@link WebSocketConnection} connection.
	 * @return The success boolean.
	 */
	public static boolean contains(final WebSocketConnection connection) {
		return instance().cache().containsKey(connection);
	}

	/**
	 * Adds new {@link WebSocketConnection} and {@link Session}.
	 * 
	 * @param connection
	 *          The {@link WebSocketConnection} connection.
	 * @param session
	 *          The {@link Session} session.
	 * @return The previous associated {@link Session}.
	 */
	public static Session add(final WebSocketConnection connection, final Session session) {
		return instance().cache().put(connection, session);
	}

	/**
	 * Removes the {@link Session} for given {@link WebSocketConnection}.
	 * 
	 * @param connection
	 *          The {@link WebSocketConnection} connection.
	 * @return The removed {@link Session}.
	 */
	public static Session remove(final WebSocketConnection connection) {
		return instance().cache().remove(connection);
	}

	/**
	 * Removes all sessions.
	 */
	public static void clear() {
		instance().cache().clear();
	}

}
