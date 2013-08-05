package game.processor.plugin;

import game.Session;
import game.network.server.GameStartPacket;
import game.processor.GameProcessor;
import game.processor.GameProcessor.State;
import game.processor.GameProcessor.Topic;
import game.processor.meta.AbstractPlugin;
import game.processor.meta.IPlugin;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import models.entity.game.Match;
import models.entity.game.MatchState;
import play.Logger;
import play.db.jpa.JPA;

import com.ssachtleben.play.plugin.event.EventBinding;
import com.ssachtleben.play.plugin.event.Events;

import dao.game.MatchDAO;

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
			eventBinding = Events.instance().register(getProcessor().getTopicName(Topic.PRELOAD), this,
					this.getClass().getMethod("update", Long.class, Integer.class));
		} catch (NoSuchMethodException | SecurityException e) {
			log.error("Failed to register observer", e);
		}
	}

	@Override
	public void unload() {
		if (eventBinding != null) {
			Events.instance().unregister(getProcessor().getTopicName(Topic.PRELOAD), eventBinding);
		}
	}

	@Override
	public void process(double delta, long now) {
		if (startTime + timeout < now) {
			log.info("Preload timeout reached for " + getProcessor().toString());
			preloadPlayerMissing = 0;
		}
		if (preloadPlayerMissing == 0) {
			log.info("All player finshed preloading for " + getProcessor().toString() + " switching game state to " + State.GAME);
			JPA.withTransaction(new play.libs.F.Callback0() {
				@Override
				public void invoke() throws Throwable {
					Match match = MatchDAO.getInstance().getById(getMatch().getId());
					match.setState(MatchState.GAME);
					match.setPreloadTime(new Date().getTime() - match.getCdate().getTime());
				}
			});
			getProcessor().publish(Topic.STATE, State.GAME);
			getProcessor().broadcast(new GameStartPacket());
		}
	}

	@Override
	public void addPlayer(Session session) {
		if (!preloadProgress.containsKey(session.getPlayerId())) {
			preloadProgress.put(session.getPlayerId(), 0);
		}
	}

	@Override
	public void removePlayer(Session session) {
		// Do nothing, the preload state should not change when a user disconnects.
		// Once the timeout will be reached the game should start automatically...
	}

	public void update(Long playerId, Integer progress) {
		if (preloadProgress.containsKey(playerId)) {
			log.info("Update preload progress for user " + playerId + " with " + progress);
			if (preloadProgress.get(playerId) == 100 && playerId < 100) {
				// Player finished preloaded before but seems reconnected and need
				// to load again
				log.info("Player " + playerId + " has reconnected");
				preloadPlayerMissing++;
			} else if (preloadProgress.get(playerId) < 100 && progress == 100) {
				// Player fully preloaded and waiting for starting game
				log.info("Player " + playerId + " finished preloading");
				preloadPlayerMissing--;
			}
			preloadProgress.replace(playerId, progress);
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
