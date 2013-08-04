package game;

import game.event.GameJoinEvent;
import game.event.GameLeaveEvent;
import game.processor.GameProcessor;
import models.entity.game.Match;
import models.entity.game.MatchToken;
import models.entity.game.Tower;
import models.entity.game.Wave;

import org.hibernate.Hibernate;
import org.webbitserver.WebSocketConnection;

import play.Logger;
import play.db.jpa.JPA;
import play.libs.Json;
import util.PacketUtils;

import com.ssachtleben.play.plugin.event.ReferenceStrength;
import com.ssachtleben.play.plugin.event.annotations.Observer;

import dao.game.MatchDAO;

/**
 * Controls all running games and allows to create new and shutdown existing games.
 * 
 * @author Alexander Wilhelmer
 * @author Sebastian Sachtleben
 */
public class Games extends Cache<Long, GameProcessor> {
	private static final Logger.ALogger log = Logger.of(Games.class);

	/**
	 * Keep private instance of {@link Games}.
	 */
	private static Games instance = new Games();

	/**
	 * Returns {@link Games} instance.
	 * 
	 * @return The {@link Games} instance.
	 */
	private static Games getInstance() {
		return instance;
	}

	/**
	 * Private constructor to prevent class others from creating {@link Games} instance.
	 */
	private Games() {
		// empty
	}

	/**
	 * Shutdown all active games and remove processors and sessions.
	 */
	public static void shutdown() {
		Sessions.clear();
		getInstance().cache().clear();
	}

	/**
	 * Invokes during receiving new client init packet and creates a new game or join an existing match.
	 * 
	 * @param event
	 *          The {@link GameJoinEvent} event.
	 */
	@Observer(topic = EventKeys.PLAYER_JOIN, referenceStrength = ReferenceStrength.STRONG)
	public static void join(final MatchToken token, final WebSocketConnection connection) {
		synchronized (getInstance().cache()) {
			final long matchId = token.getResult().getMatch().getId();
			if (!getInstance().cache().containsKey(matchId)) {
				log.info("Create game processor for match " + matchId);
				JPA.withTransaction(new play.libs.F.Callback0() {
					@Override
					public void invoke() throws Throwable {
						Match match = MatchDAO.getInstance().getById(matchId);
						Hibernate.initialize(match.getPlayerResults());
						Hibernate.initialize(match.getMap().getTowers());
						for (Tower tower : match.getMap().getTowers()) {
							Hibernate.initialize(tower.getWeapons());
						}
						Hibernate.initialize(match.getMap().getWaves());
						for (Wave wave : match.getMap().getWaves()) {
							Hibernate.initialize(wave.getPath().getDbWaypoints());
							Hibernate.initialize(wave.getUnits());
							Hibernate.initialize(wave.getPath());
						}
						final GameProcessor game = new GameProcessor(match);
						getInstance().cache().put(matchId, game);
					}
				});
			}
			log.info("Join match " + matchId);
			final GameProcessor game = getInstance().cache().get(matchId);
			Session session = new Session(game.getMatch(), token.getPlayer(), token, connection);
			session.setGame(game);
			Sessions.add(connection, session);
			game.addPlayer(session);
			log.info(String.format("Player '<%s>' attempt to join game '<%s>'", token.getPlayer().getUser().getUsername(), game.getTopicName()));
			JPA.withTransaction(new play.libs.F.Callback0() {
				@Override
				public void invoke() throws Throwable {
					connection.send(Json.toJson(PacketUtils.createPreloadDataPacket(connection, game)).toString());
				}
			});
		}
	}

	/**
	 * Invokes on disconnected websocket connection and remove player from match.
	 * 
	 * @param event
	 *          The {@link GameLeaveEvent} event.
	 */
	@Observer(topic = EventKeys.PLAYER_LEAVE, referenceStrength = ReferenceStrength.STRONG)
	public static void leave(final WebSocketConnection connection) {
		log.info("Remove player with connection " + connection.httpRequest().id());
		if (!Sessions.contains(connection)) {
			log.error("Couldn't find connection " + connection.httpRequest().id());
			return;
		}
		Session session = Sessions.get(connection);
		if (session != null) {
			session.getGame().removePlayer(connection);
			Sessions.remove(connection);
		}
	}
}
