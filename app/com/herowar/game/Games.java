package com.herowar.game;


import org.hibernate.Hibernate;

import play.Logger;

import com.herowar.dao.game.MatchDAO;
import com.herowar.game.network.Connection;
import com.herowar.game.network.Connections;
import com.herowar.game.processor.GameProcessor;
import com.herowar.models.entity.game.Match;
import com.herowar.models.entity.game.MatchToken;
import com.herowar.models.entity.game.Tower;
import com.herowar.models.entity.game.Wave;
import com.herowar.util.PacketUtils;
import com.ssachtleben.play.plugin.event.ReferenceStrength;
import com.ssachtleben.play.plugin.event.annotations.Observer;


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
		Connections.clear();
		getInstance().cache().clear();
	}

	@Observer(topic = EventKeys.PLAYER_JOIN, referenceStrength = ReferenceStrength.STRONG)
	public static void join(final Connection connection, final MatchToken token) {
		synchronized (getInstance().cache()) {
			final long matchId = token.getResult().getMatch().getId();
			if (!getInstance().cache().containsKey(matchId)) {
				log.info(String.format("Create match %s ", matchId));
				Match match = MatchDAO.getInstance().getById(matchId);
				log.info(String.format("Found %s ", match));
				Hibernate.initialize(match.getPlayerResults());
				log.info(String.format("Found %s ", match.getPlayerResults()));
				Hibernate.initialize(match.getMap().getTowers());
				log.info(String.format("Found %s ", match.getMap().getTowers()));
				for (Tower tower : match.getMap().getTowers()) {
					Hibernate.initialize(tower.getWeapons());
					log.info(String.format("Found %s ", tower.getWeapons()));
				}
				Hibernate.initialize(match.getMap().getWaves());
				log.info(String.format("Found %s ", match.getMap().getWaves()));
				for (Wave wave : match.getMap().getWaves()) {
					Hibernate.initialize(wave.getPath().getDbWaypoints());
					log.info(String.format("Found %s ", wave.getPath().getDbWaypoints()));
					Hibernate.initialize(wave.getUnits());
					log.info(String.format("Found %s ", wave.getUnits()));
					Hibernate.initialize(wave.getPath());
					log.info(String.format("Found %s ", wave.getPath()));
				}
				final GameProcessor game = new GameProcessor(match);
				getInstance().cache().put(matchId, game);
			}
			log.info(String.format("Join match %s ", matchId));
			final GameProcessor game = getInstance().cache().get(matchId);
			connection.game(game);
			game.add(connection);
			log.info(String.format("Player '<%s>' attempt to join game '<%s>'", token.getPlayer().getUser().getUsername(), game.getTopicName()));
			// JPA.withTransaction(new play.libs.F.Callback0() {
			// @Override
			// public void invoke() throws Throwable {
			connection.send(PacketUtils.createPreloadDataPacket(game));
			// }
			// });
		}
	}

	@Observer(topic = EventKeys.PLAYER_LEAVE, referenceStrength = ReferenceStrength.STRONG)
	public static void leave(final Connection connection) {
		log.info(String.format("Remove %s", connection));
		connection.game().remove(connection);
		connection.close();
	}
}
