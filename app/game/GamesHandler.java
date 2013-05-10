package game;

import game.event.GameJoinEvent;
import game.event.GameLeaveEvent;
import game.network.server.PreloadData;
import game.network.server.PreloadDataPacket;
import game.processor.GameProcessor;
import game.processor.ProcessorHandler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import models.entity.game.Map;

import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;
import org.webbitserver.WebSocketConnection;

import play.Logger;
import play.libs.Json;

/**
 * The GamesHandler control all current running games.
 * 
 * @author Alexander Wilhelmer
 * @author Sebastian Sachtleben
 */
@SuppressWarnings("serial")
public class GamesHandler implements Serializable {

  private static final Logger.ALogger log = Logger.of(GamesHandler.class);

  private ConcurrentHashMap<Long, List<GameProcessor>> games = new ConcurrentHashMap<Long, List<GameProcessor>>();
  private ConcurrentHashMap<WebSocketConnection, GameSession> connections = new ConcurrentHashMap<WebSocketConnection, GameSession>();
  private ConcurrentHashMap<GameSession, ProcessorHandler> processors = new ConcurrentHashMap<GameSession, ProcessorHandler>();

  private long gameId = 1;

  private static GamesHandler instance = new GamesHandler();

  public static GamesHandler getInstance() {
    return instance;
  }

  private GamesHandler() {
    AnnotationProcessor.process(this);
    log.info(this.getClass().getSimpleName() + " initialized");
  }

  @EventSubscriber
  public void observePlayerJoinEvent(GameJoinEvent event) {
    GameSession session = new GameSession(event.getGameToken().getCreatedByUser(), event.getGameToken(), event.getConnection());
    GameProcessor game = createGame(event.getGameToken().getMap(), session);
    session.setGame(game);
    connections.put(event.getConnection(), session);
    log.info(String.format("Player '<%s>' attempt to join game '<%s>'", event.getGameToken().getCreatedByUser().getUsername(), game.getTopicName()));
    sendPreloadDataPacket(event.getConnection(), game);
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

  private GameProcessor createGame(Map map, GameSession session) {
    GameProcessor game = getOpenGame(map);
    if (game != null) {
      game.addPlayer(session);
    } else {
      game = new GameProcessor(gameId, map, session);
      game.start();
      gameId++;
      if (games.containsKey(map.getId())) {
        games.get(map.getId()).add(game);
      } else {
        List<GameProcessor> newGameList = new ArrayList<GameProcessor>();
        newGameList.add(game);
        games.put(map.getId(), newGameList);
      }
    }
    return game;
  }

  private GameProcessor getOpenGame(Map map) {
    if (games.containsKey(map.getId())) {
      List<GameProcessor> processors = games.get(map.getId());
      for (GameProcessor game : processors) {
        if (game.getSessions().size() < map.getTeamSize()) {
          return game;
        }
      }
    }
    return null;
  }

  private void sendPreloadDataPacket(WebSocketConnection connection, GameProcessor game) {
    // TODO: replace hardcoded preload data
    java.util.Map<String, String> textures = new HashMap<String, String>();
    textures.put("stone-natural-001", "assets/images/game/textures/stone/natural-001.jpg");
    textures.put("stone-rough-001", "assets/images/game/textures/stone/rough-001.jpg");
    java.util.Map<String, String> texturesCube = new HashMap<String, String>();
    texturesCube.put("default", "assets/images/game/skybox/default/%1.jpg");
    java.util.Map<String, String> geometries = new HashMap<String, String>();
    geometries.put("Ratamahatta", "api/game/geometry/unit/2");
    PreloadDataPacket packet = new PreloadDataPacket(game.getMap().getId(), new PreloadData(textures, texturesCube, geometries));
    connection.send(Json.toJson(packet).toString());
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
    // TODO: Current we shutdown the games without users, later they should go
    // on...
    if (game.getSessions().size() == 0) {
      game.stop();
      games.get(game.getMap().getId()).remove(game);
    }
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

  public java.util.Map<Long, List<GameProcessor>> getGames() {
    return games;
  }

  public java.util.Map<WebSocketConnection, GameSession> getConnections() {
    return connections;
  }

  public ConcurrentHashMap<GameSession, ProcessorHandler> getProcessors() {
    return processors;
  }
}
