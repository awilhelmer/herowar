package game.processor;

import game.Clock;
import game.network.server.PreloadDataPacket;
import game.processor.GameProcessor.State;

import com.ssachtleben.play.plugin.event.EventBinding;

import models.entity.game.Map;
import models.entity.game.Match;

/**
 * Contains game depending properties to control the game behavior.
 * 
 * @author Sebastian Sachtleben
 */
public class GameData {

	private long id;
	private Match match;
	private Map map;
	private Clock clock = new Clock();
	private PreloadDataPacket preloadPacket = null;
	private EventBinding eventBinding;
	private boolean wavesFinished = false;
	private boolean unitsFinished = false;
	private boolean waveRequest = false;
	private boolean tutorialUpdate = false;
	private boolean updateGold = false;
	private State state;

	public GameData(Match match) {
		this.match = match;
		this.map = match.getMap();
	}

}
