package game;

import models.entity.game.MatchToken;

import org.webbitserver.WebSocketConnection;

/**
 * Contains all used event keys.
 * 
 * @author Sebastian Sachtleben
 */
public abstract class EventKeys {

	/**
	 * Published for new player with payload: {@link MatchToken}, {@link WebSocketConnection}
	 */
	public static final String PLAYER_JOIN = "player-join";

	/**
	 * Published for leaving player with payload: {@link WebSocketConnection}
	 */
	public static final String PLAYER_LEAVE = "player-leave";

}
