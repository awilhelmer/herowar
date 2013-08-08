package com.herowar.game.processor.plugin;


import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import play.Logger;
import play.db.jpa.JPA;

import com.herowar.dao.game.MatchDAO;
import com.herowar.game.network.Connection;
import com.herowar.game.network.server.GameStartPacket;
import com.herowar.game.processor.GameProcessor;
import com.herowar.game.processor.GameProcessor.State;
import com.herowar.game.processor.GameProcessor.Topic;
import com.herowar.game.processor.meta.AbstractPlugin;
import com.herowar.game.processor.meta.IPlugin;
import com.herowar.models.entity.game.Match;
import com.herowar.models.entity.game.MatchState;
import com.ssachtleben.play.plugin.event.EventBinding;
import com.ssachtleben.play.plugin.event.Events;


/**
 * The PreloadUpdatePlugin sends informations about the preload state of every connected player.
 * 
 * @author Sebastian Sachtleben
 */
public class PreloadPlugin extends AbstractPlugin implements IPlugin {
	private final static Logger.ALogger log = Logger.of(PreloadPlugin.class);
	private final static int timeout = 1000 * 60 * 5;

	private ConcurrentHashMap<Long, Integer> preloadProgress = new ConcurrentHashMap<Long, Integer>();

	private Integer preloadPlayerMissing = 0;
	private long startTime = new Date().getTime();
	private EventBinding eventBinding;

	public PreloadPlugin(GameProcessor processor) {
		super(processor);
		preloadPlayerMissing = getMatch().getPlayerResults().size();
		log.info("Start preloading phase for " + preloadPlayerMissing + " players");
	}

	@Override
	public void load() {
		try {
			eventBinding = Events.instance().register(game().getTopicName(Topic.PRELOAD), this,
					this.getClass().getMethod("update", Connection.class, Integer.class));
		} catch (NoSuchMethodException | SecurityException e) {
			log.error("Failed to register observer", e);
		}
	}

	@Override
	public void unload() {
		if (eventBinding != null) {
			Events.instance().unregister(game().getTopicName(Topic.PRELOAD), eventBinding);
		}
	}

	@Override
	public void process(double delta, long now) {
		if (startTime + timeout < now) {
			log.info("Preload timeout reached for " + game().toString());
			preloadPlayerMissing = 0;
		}
		if (preloadPlayerMissing == 0) {
			log.info("All player finshed preloading for " + game().toString() + " switching game state to " + State.GAME);
			JPA.withTransaction(new play.libs.F.Callback0() {
				@Override
				public void invoke() throws Throwable {
					Match match = MatchDAO.getInstance().getById(getMatch().getId());
					match.setState(MatchState.GAME);
					match.setPreloadTime(new Date().getTime() - match.getCdate().getTime());
				}
			});
			game().publish(Topic.STATE, State.GAME);
			game().broadcast(new GameStartPacket());
		}
	}

	@Override
	public void add(Connection connection) {
		if (!preloadProgress.containsKey(connection.id())) {
			preloadProgress.put(connection.id(), 0);
		}
	}

	@Override
	public void remove(Connection connection) {
		// Do nothing, the preload state should not change when a user disconnects.
		// Once the timeout will be reached the game should start automatically...
	}

	public void update(Connection connection, Integer progress) {
		if (preloadProgress.containsKey(connection.id())) {
			log.info("Update preload progress for user " + connection.id() + " with " + progress);
			if (preloadProgress.get(connection.id()) == 100 && connection.id() < 100) {
				// Player finished preloaded before but seems reconnected and need
				// to load again
				log.info("Player " + connection.id() + " has reconnected");
				preloadPlayerMissing++;
			} else if (preloadProgress.get(connection.id()) < 100 && progress == 100) {
				// Player fully preloaded and waiting for starting game
				log.info("Player " + connection.id() + " finished preloading");
				preloadPlayerMissing--;
			}
			preloadProgress.replace(connection.id(), progress);
		}
	}

	@Override
	public State onState() {
		return State.PRELOAD;
	}

	@Override
	public String toString() {
		return "PreloadPlugin";
	}
}
