package game.event;

import game.processor.GameProcessor.State;

/**
 * The GameStateEvent will be fired to change the state of a game.
 * 
 * @author Sebastian Sachtleben
 */
public class GameStateEvent {

	private State state;

	public GameStateEvent(State state) {
		this.state = state;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}
}
