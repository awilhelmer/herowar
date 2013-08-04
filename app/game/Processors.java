package game;

import game.processor.ProcessorHandler;

import org.webbitserver.WebSocketConnection;

/**
 * Handles all {@link ProcessorHandler} from each {@link Session}.
 * 
 * @author Sebastian Sachtleben
 */
public class Processors extends Cache<Session, ProcessorHandler> {

	/**
	 * Keep private instance of {@link Processors}.
	 */
	private static Processors instance = new Processors();

	/**
	 * Returns {@link Processors} instance.
	 * 
	 * @return The {@link Processors} instance.
	 */
	private static Processors instance() {
		return instance;
	}

	/**
	 * Private constructor to prevent class others from creating {@link Processors} instance.
	 */
	private Processors() {
		// empty
	}

	/**
	 * Get {@link ProcessorHandler} for given {@link Session}.
	 * 
	 * @param session
	 *          The {@link Session} session.
	 * @return The matching {@link ProcessorHandler}.
	 */
	public static ProcessorHandler get(final Session session) {
		return instance().cache().get(session);
	}

	/**
	 * Checks if given {@link Session} exists.
	 * 
	 * @param session
	 *          The {@link Session} session.
	 * @return The success boolean.
	 */
	public static boolean contains(final Session session) {
		return instance().cache().containsKey(session);
	}

	/**
	 * Adds new {@link WebSocketConnection} and {@link Session}.
	 * 
	 * @param session
	 *          The {@link Session} session.
	 * @param handler
	 *          The {@link ProcessorHandler} handler.
	 * @return The previous associated {@link Session}.
	 */
	public static ProcessorHandler add(final Session session, final ProcessorHandler handler) {
		return instance().cache().put(session, handler);
	}

	/**
	 * Removes the {@link ProcessorHandler} for given {@link Session}.
	 * 
	 * @param session
	 *          The {@link Session} session.
	 * @return The removed {@link ProcessorHandler}.
	 */
	public static ProcessorHandler remove(final Session session) {
		ProcessorHandler handler = instance().cache().get(session);
		if (handler != null && handler.isStarted()) {
			handler.stop();
		}
		return instance().cache().remove(session);
	}

	/**
	 * Stops and removes all {@link ProcessorHandler}.
	 */
	public static void clear() {
		for (ProcessorHandler handler : instance().cache().values()) {
			handler.stop();
		}
		instance().cache().clear();
	}

}
