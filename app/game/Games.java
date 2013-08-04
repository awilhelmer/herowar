package game;

import game.event.GameJoinEvent;
import game.event.GameLeaveEvent;
import game.network.server.PreloadData;
import game.network.server.PreloadDataPacket;
import game.processor.GameProcessor;

import java.util.HashMap;
import java.util.Iterator;

import models.entity.game.Match;
import models.entity.game.MatchToken;
import models.entity.game.Tower;
import models.entity.game.TowerWeapon;
import models.entity.game.TowerWeaponType;
import models.entity.game.Unit;
import models.entity.game.Wave;

import org.hibernate.Hibernate;
import org.webbitserver.WebSocketConnection;

import play.Logger;
import play.db.jpa.JPA;
import play.libs.Json;

import com.ssachtleben.play.plugin.event.Events;

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
	 * Contains event topic name to publish join and leave events via {@link Events}.
	 */
	public static final String EVENT_TOPIC = Games.class.getSimpleName();

	/**
	 * Keep private instance of {@link Games}.
	 */
	private static Games instance = new Games();

	/**
	 * Returns {@link Games} instance.
	 * 
	 * @return The {@link Games} instance.
	 */
	public static Games getInstance() {
		return instance;
	}

	/**
	 * Private constructor to prevent class others from creating {@link Games} instance.
	 */
	private Games() {
		try {
			Events.instance().register(EVENT_TOPIC, this, this.getClass().getMethod("join", GameJoinEvent.class));
			Events.instance().register(EVENT_TOPIC, this, this.getClass().getMethod("leave", GameLeaveEvent.class));
			log.info(this.getClass().getSimpleName() + " initialized");
		} catch (NoSuchMethodException | SecurityException e) {
			log.error("Failed to register observer", e);
		}
	}

	/**
	 * Invokes during receiving new client init packet and creates a new game or join an existing match.
	 * 
	 * @param event
	 *          The {@link GameJoinEvent} event.
	 */
	public void join(final GameJoinEvent event) {
		synchronized (cache()) {
			if (!cache().containsKey(event.getMatchId())) {
				log.info("Create game processor for match " + event.getMatchId());
				createMatch(event.getMatchId());
			}
			log.info("Join match " + event.getMatchId());
			joinMatch(event.getMatchId(), event.getToken(), event.getConnection());
		}
	}

	/**
	 * Invokes on disconnected websocket connection and remove player from match.
	 * 
	 * @param event
	 *          The {@link GameLeaveEvent} event.
	 */
	public void leave(final GameLeaveEvent event) {
		log.info("Remove player with connection " + event.getConnection().httpRequest().id());
		if (!Sessions.contains(event.getConnection())) {
			log.error("Couldn't find connection " + event.getConnection().httpRequest().id());
			return;
		}
		Session player = Sessions.get(event.getConnection());
		if (player != null) {
			removePlayer(player, event.getConnection());
		}
	}

	private void createMatch(final long matchId) {
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
				GameProcessor game = new GameProcessor(match);
				cache().put(matchId, game);
			}
		});
	}

	private void joinMatch(final long matchId, final MatchToken token, final WebSocketConnection connection) {
		final GameProcessor game = cache().get(matchId);
		Session session = new Session(game.getMatch(), token.getPlayer(), token, connection);
		Sessions.add(connection, session);
		session.setGame(game);
		game.addPlayer(session);
		log.info(String.format("Player '<%s>' attempt to join game '<%s>'", token.getPlayer().getUser().getUsername(), game.getTopicName()));
		JPA.withTransaction(new play.libs.F.Callback0() {
			@Override
			public void invoke() throws Throwable {
				sendPreloadDataPacket(connection, game);
			}
		});
	}

	private void sendPreloadDataPacket(final WebSocketConnection connection, final GameProcessor game) {
		if (game.getPreloadPacket() == null) {
			java.util.Map<String, String> images = new HashMap<String, String>();
			java.util.Map<String, String> textures = new HashMap<String, String>();
			textures.put("ground-rock", "assets/images/game/textures/ground/rock.jpg");
			textures.put("ground-grass", "assets/images/game/textures/ground/grass.jpg");

			textures.put("texture_ground_grass", "assets/images/game/textures/ground/texture_ground_grass.jpg");
			textures.put("texture_ground_bare", "assets/images/game/textures/ground/texture_ground_bare.jpg");
			textures.put("texture_ground_snow", "assets/images/game/textures/ground/texture_ground_snow.jpg");
			// textures.put("stone-natural-001", "assets/images/game/textures/stone/natural-001.jpg");
			// textures.put("stone-rough-001", "assets/images/game/textures/stone/rough-001.jpg");
			java.util.Map<String, String> texturesCube = new HashMap<String, String>();
			if (game.getMap().getSkybox() != null && !"".equals(game.getMap().getSkybox())) {
				String skybox = game.getMap().getSkybox();
				texturesCube.put(skybox, "assets/images/game/skybox/" + skybox + "/%1.jpg");
			}
			java.util.Map<String, String> geometries = new HashMap<String, String>();
			Iterator<Wave> iter = game.getMap().getWaves().iterator();
			while (iter.hasNext()) {
				Wave wave = iter.next();
				Iterator<Unit> iter2 = wave.getUnits().iterator();
				while (iter2.hasNext()) {
					Unit unit = iter2.next();
					geometries.put(unit.getName(), "api/game/geometry/unit/" + unit.getId());
					if (unit.getExplode() && !images.containsKey("explosion")) {
						images.put("explosion", "assets/images/game/textures/effects/explosion.png");
					}
					if (unit.getBurning() && !images.containsKey("cloud10")) {
						textures.put("cloud10", "assets/images/game/textures/effects/cloud10.png");
					}
				}
			}
			Iterator<Tower> iter3 = game.getMap().getTowers().iterator();
			while (iter3.hasNext()) {
				Tower tower = iter3.next();
				geometries.put(tower.getName(), "api/game/geometry/tower/" + tower.getId());
				for (TowerWeapon weapon : tower.getWeapons()) {
					if (TowerWeaponType.LASER.equals(weapon.getType()) && !geometries.containsKey("particle001")) {
						textures.put("particle001", "assets/images/game/textures/effects/particle001.png");
					}
					if (TowerWeaponType.ROCKET.equals(weapon.getType()) && !geometries.containsKey("rocket")) {
						geometries.put("rocket", "assets/geometries/weapons/rocket.js");
					}
					if (TowerWeaponType.ROCKET.equals(weapon.getType()) && !images.containsKey("explosion")) {
						images.put("explosion", "assets/images/game/textures/effects/explosion.png");
					}
				}
			}
			game.setPreloadPacket(new PreloadDataPacket(game.getMap().getId(), new PreloadData(images, textures, texturesCube, geometries)));
		}
		connection.send(Json.toJson(game.getPreloadPacket()).toString());
	}

	private void removePlayer(final Session session, final WebSocketConnection connection) {
		session.getGame().removePlayer(connection);
		Sessions.remove(connection);
		Processors.remove(session);
	}

	public void stop() {
		Sessions.clear();
		Processors.clear();
		cache().clear();
	}
}
