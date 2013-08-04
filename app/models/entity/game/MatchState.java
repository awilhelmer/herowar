package models.entity.game;

/**
 * The Match states are very important and tell us which state is currently in that match:
 * 
 * <p>
 * INIT - The game is in match making mode.
 * </p>
 * <p>
 * PRELOAD - All users preload needed content.
 * </p>
 * <p>
 * GAME - The game is running.
 * </p>
 * <p>
 * FINISH - The game is done.
 * </p>
 * 
 * @author Sebastian Sachtleben
 * 
 */
public enum MatchState {
	INIT, PRELOAD, GAME, FINISH
}
