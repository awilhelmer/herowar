package com.herowar.game.processor.plugin;


import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import com.herowar.dao.game.MatchDAO;
import com.herowar.game.network.Connection;
import com.herowar.game.network.server.GameDefeatPacket;
import com.herowar.game.network.server.GameVictoryPacket;
import com.herowar.game.processor.CacheConstants;
import com.herowar.game.processor.GameProcessor;
import com.herowar.game.processor.GameProcessor.State;
import com.herowar.game.processor.meta.AbstractPlugin;
import com.herowar.game.processor.meta.IPlugin;
import com.herowar.models.entity.game.Match;
import com.herowar.models.entity.game.MatchResult;
import com.herowar.models.entity.game.MatchState;

import play.Logger;
import play.db.jpa.JPA;

/**
 * The FinishPlugin sends informations about the state of the game and clean up.
 * 
 * @author Sebastian Sachtleben
 */
public class FinishPlugin extends AbstractPlugin implements IPlugin {
	private final static Logger.ALogger log = Logger.of(FinishPlugin.class);

	private long finishTimer = -1;
	private boolean done = false;

	public FinishPlugin(GameProcessor processor) {
		super(processor);
	}

	@Override
	public void process(double delta, long now) {
		if (finishTimer == -1) {
			finishTimer = now;
		}
		if (!done && finishTimer + 2000 <= now) {
			done = true;
			finishTimer = now;
			if (getMap().getLives() <= 0) {
				GameDefeatPacket packet = new GameDefeatPacket();
				broadcast(packet);
				log.info("<" + game().getTopicName() + "> Game is defeated");
			} else {
				GameVictoryPacket packet = new GameVictoryPacket();
				broadcast(packet);
				log.info("<" + game().getTopicName() + "> Game is victory");
			}
			saveResults();
		} else if (done && finishTimer + 2000 <= now) {
			// TODO: stop properly the game here and cleanup
			game().stop();
		}
	}

	private void saveResults() {
		final boolean victory = getMap().getLives() > 0;
		final int lives = getMap().getLives();
		JPA.withTransaction(new play.libs.F.Callback0() {
			@Override
			public void invoke() throws Throwable {
				Match match = MatchDAO.getInstance().getById(getMatch().getId());
				match.setState(MatchState.FINISH);
				match.setVictory(victory);
				match.setLives(lives);
				match.setGameTime(new Date().getTime() - (match.getCdate().getTime() + match.getPreloadTime()));
				Iterator<MatchResult> results = match.getPlayerResults().iterator();
				while (results.hasNext()) {
					MatchResult result = results.next();
					ConcurrentHashMap<String, Object> cache = getPlayerCache(result.getPlayer().getId());
					result.setScore(Math.round(Double.parseDouble(cache.get(CacheConstants.SCORE).toString())));
					result.setKills(Long.parseLong(cache.get(CacheConstants.KILLS).toString()));
					result.getToken().setInvalid(true);
				}
			}
		});
	}

	@Override
	public void add(Connection connection) {
		// TODO Auto-generated method stub
	}

	@Override
	public void remove(Connection connection) {
		// TODO Auto-generated method stub
	}

	@Override
	public State onState() {
		return State.FINISH;
	}

	@Override
	public String toString() {
		return "FinishPlugin";
	}
}
