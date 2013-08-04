package game.network;

import game.Cache;
import models.entity.game.Player;

import org.webbitserver.WebSocketConnection;

/**
 * Handles all authorized connections.
 * 
 * @author Sebastian Sachtleben
 * 
 */
public class Connections extends Cache<WebSocketConnection, Player> {

	/**
	 * Keep private instance of {@link Connections}.
	 */
	private static Connections instance = new Connections();

	/**
	 * @return The {@link Connections} instance.
	 */
	private static Connections instance() {
		return instance;
	}

	/**
	 * Private constructor to prevent class others from creating {@link Connections} instance.
	 */
	private Connections() {
	}

	/**
	 * Get {@link Player} for given {@link WebSocketConnection}.
	 * 
	 * @param connection
	 *          The {@link WebSocketConnection} connection.
	 * @return The matching {@link Player}.
	 */
	public static Player get(final WebSocketConnection connection) {
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
	 * Adds new {@link WebSocketConnection} and {@link Player}.
	 * 
	 * @param connection
	 *          The {@link WebSocketConnection} connection.
	 * @param player
	 *          The {@link Player} player.
	 * @return The previous associated {@link Player}.
	 */
	public static Player add(final WebSocketConnection connection, final Player player) {
		return instance().cache().put(connection, player);
	}

	/**
	 * Removes the {@link Player} for given {@link WebSocketConnection}.
	 * 
	 * @param connection
	 *          The {@link WebSocketConnection} connection.
	 * @return The removed {@link Player}.
	 */
	public static Player remove(final WebSocketConnection connection) {
		return instance().cache().remove(connection);
	}

	/**
	 * Returns the number of key-value mappings in this map. If the map contains more than Integer.MAX_VALUE elements, returns
	 * Integer.MAX_VALUE.
	 * 
	 * @return the number of key-value mappings in this map
	 */
	public static int size() {
		return instance().cache().size();
	}

	/**
	 * Removes all connections.
	 */
	public static void clear() {
		instance().cache().clear();
	}

}
