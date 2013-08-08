package com.herowar.game.network;

import com.herowar.game.Cache;
import com.herowar.models.entity.game.Player;


/**
 * Handles all authorized connections.
 * 
 * @author Sebastian Sachtleben
 * 
 */
public class Connections extends Cache<Long, Connection> {

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
	 * Get {@link Player} for given {@link Long} id.
	 * 
	 * @param id
	 *          The {@link Long} id.
	 * @return The matching {@link Connection}.
	 */
	public static Connection get(final Long id) {
		return instance().cache().get(id);
	}

	/**
	 * Checks if given {@link Long} id exists.
	 * 
	 * @param connection
	 *          The {@link Long} connection.
	 * @return The success boolean.
	 */
	public static boolean contains(final Long id) {
		return instance().cache().containsKey(id);
	}

	/**
	 * Adds new {@link Long} id and {@link Connection} connection.
	 * 
	 * @param id
	 *          The {@link Long} id.
	 * @param connection
	 *          The {@link Connection} connection.
	 * @return The previous associated {@link Connection}.
	 */
	public static Connection add(final Long id, final Connection connection) {
		return instance().cache().put(id, connection);
	}

	/**
	 * Removes the {@link Connection} connection for given {@link Long} id.
	 * 
	 * @param id
	 *          The {@link Long} id.
	 * @return The removed {@link Player}.
	 */
	public static Connection remove(final Long id) {
		return instance().cache().remove(id);
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
