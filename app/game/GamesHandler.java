package game;

import game.event.GameJoinEvent;
import game.event.GameLeaveEvent;
import game.network.server.PreloadData;
import game.network.server.PreloadDataPacket;
import game.processor.GameProcessor;
import game.processor.ProcessorHandler;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import models.entity.game.Match;
import models.entity.game.MatchToken;
import models.entity.game.Tower;
import models.entity.game.TowerWeapon;
import models.entity.game.TowerWeaponType;
import models.entity.game.Unit;
import models.entity.game.Wave;

import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;
import org.hibernate.Hibernate;
import org.webbitserver.WebSocketConnection;

import play.Logger;
import play.db.jpa.JPA;
import play.libs.Json;
import dao.game.MatchDAO;

/**
 * The GamesHandler control all current running games.
 * 
 * @author Alexander Wilhelmer
 * @author Sebastian Sachtleben
 */
@SuppressWarnings("serial")
public class GamesHandler implements Serializable {

  private static final Logger.ALogger log = Logger.of(GamesHandler.class);

  private ConcurrentHashMap<Long, GameProcessor> games = new ConcurrentHashMap<Long, GameProcessor>();
  private ConcurrentHashMap<WebSocketConnection, GameSession> connections = new ConcurrentHashMap<WebSocketConnection, GameSession>();
  private ConcurrentHashMap<GameSession, ProcessorHandler> processors = new ConcurrentHashMap<GameSession, ProcessorHandler>();

  private static GamesHandler instance = new GamesHandler();

  public static GamesHandler getInstance() {
    return instance;
  }

  private GamesHandler() {
    AnnotationProcessor.process(this);
    log.info(this.getClass().getSimpleName() + " initialized");
  }

  @EventSubscriber
  public void observePlayerJoinEvent(final GameJoinEvent event) {
    synchronized (games) {
      if (!games.containsKey(event.getMatchId())) {
        log.info("Create game processor for match " + event.getMatchId());
        matchCreate(event.getMatchId());
      }
      log.info("Join match " + event.getMatchId());
      matchJoin(event.getMatchId(), event.getToken(), event.getConnection());
    }
  }

  @EventSubscriber
  public void observePlayerLeaveEvent(GameLeaveEvent event) {
    log.info("Remove player with connection " + event.getConnection().httpRequest().id());
    if (!connections.containsKey(event.getConnection())) {
      log.error("Couldn't find connection " + event.getConnection().httpRequest().id());
      return;
    }
    GameSession player = connections.get(event.getConnection());
    if (player != null) {
      removePlayer(player, event.getConnection());
    }
  }

  private void matchCreate(final long matchId) {
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
        games.put(matchId, game);
      }
    });
  }

  private void matchJoin(final long matchId, final MatchToken token, final WebSocketConnection connection) {
    final GameProcessor game = games.get(matchId);
    GameSession session = new GameSession(game.getMatch(), token.getPlayer(), token, connection);
    connections.put(connection, session);
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
      images.put("explosion", "assets/images/game/textures/effects/explosion.png");
      java.util.Map<String, String> textures = new HashMap<String, String>();
      textures.put("ground-rock", "assets/images/game/textures/ground/rock.jpg");
      textures.put("ground-grass", "assets/images/game/textures/ground/grass.jpg");
      //textures.put("stone-natural-001", "assets/images/game/textures/stone/natural-001.jpg");
      //textures.put("stone-rough-001", "assets/images/game/textures/stone/rough-001.jpg");
      textures.put("particle001", "assets/images/game/textures/effects/particle001.png");
      textures.put("cloud10", "assets/images/game/textures/effects/cloud10.png");
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
        }
      }
      Iterator<Tower> iter3 = game.getMap().getTowers().iterator();
      while (iter3.hasNext()) {
        Tower tower = iter3.next();
        geometries.put(tower.getName(), "api/game/geometry/tower/" + tower.getId());
        for (TowerWeapon weapon : tower.getWeapons()) {
          if (TowerWeaponType.ROCKET.equals(weapon.getType()) && !geometries.containsKey("rocket")) {
            geometries.put("rocket", "assets/geometries/weapons/rocket.js");
          }
        }
      }
      game.setPreloadPacket(new PreloadDataPacket(game.getMap().getId(), new PreloadData(images, textures, texturesCube, geometries)));
    }
    connection.send(Json.toJson(game.getPreloadPacket()).toString());
  }

  private void removePlayer(GameSession session, WebSocketConnection connection) {
    ProcessorHandler handler = processors.get(session);
    if (handler != null && handler.isStarted()) {
      handler.stop();
    }
    GameProcessor game = session.getGame();
    game.removePlayer(connection);
    connections.remove(connection);
    processors.remove(session);
  }

  public void stop() {
    for (ProcessorHandler handler : processors.values()) {
      handler.stop();
    }
    connections.clear();
    processors.clear();
    games.clear();
  }

  // GETTER && SETTER //

  public java.util.Map<Long, GameProcessor> getGames() {
    return games;
  }

  public java.util.Map<WebSocketConnection, GameSession> getConnections() {
    return connections;
  }

  public ConcurrentHashMap<GameSession, ProcessorHandler> getProcessors() {
    return processors;
  }
}
