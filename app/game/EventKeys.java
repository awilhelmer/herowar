package game;

import game.network.Connection;
import models.entity.game.MatchToken;

/**
 * Contains all used event keys.
 * 
 * @author Sebastian Sachtleben
 */
public abstract class EventKeys {

	/**
	 * Published for new player with payload: {@link Connection}, {@link MatchToken}
	 */
	public static final String PLAYER_JOIN = "player-join";

	/**
	 * Published for leaving player with payload: {@link Connection}
	 */
	public static final String PLAYER_LEAVE = "player-leave";

}
