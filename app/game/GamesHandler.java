package game;

import game.event.GameJoinEvent;
import game.event.GameLeaveEvent;
import game.processor.GameProcessor;
import game.processor.ProcessorHandler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import models.entity.game.Map;

import org.bushe.swing.event.annotation.AnnotationProcessor;
import org.bushe.swing.event.annotation.EventSubscriber;
import org.webbitserver.WebSocketConnection;

import play.Logger;


/**
 * 
 * @author Alexander Wilhelmer
 *
 */
public class GamesHandler implements Serializable {
  private static final long serialVersionUID = -1236371535292715843L;

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


//  private GameProcessor createGame(Episode episode, GameSession session) {
//    GameProcessor game = getOpenGame(episode);
//    if (game != null) {
//      game.addPlayer(session);
//
//    } else {
//      game = new GameProcessor(gameId, episode, session);
//      game.start();
//      gameId++;
//      if (games.containsKey(episode.getId())) {
//        games.get(episode.getId()).add(game);
//      } else {
//        List<GameProcessor> newGameList = new ArrayList<GameProcessor>();
//        newGameList.add(game);
//        games.put(episode.getId(), newGameList);
//      }
//    }
//    return game;
//  }
//
//  private GameProcessor getOpenGame(Episode episode) {
//    if (games.containsKey(episode.getId())) {
//      List<GameProcessor> processors = games.get(episode.getId());
//      for (GameProcessor game : processors) {
//        if (game.getSessions().size() < episode.getMaxPlayers()) {
//          return game;
//        }
//      }
//    }
//    return null;
//  }
//
//  private void sendPreloadListPacket(WebSocketConnection connection, GameProcessor game) {
//    Set<String> modelNames = new HashSet<String>();
//    for (GameSession obj : game.getSessions()) {
//      modelNames.add(obj.getModelName());
//    }
//    for (SceneObject obj : game.getObjects()) {
//      modelNames.add(obj.getModelName());
//    }
//    Gson gson = GsonProducer.get();
//    connection.send(gson.toJson(new PreloadListPacket(game.getTopic(),
//        modelNames.toArray(new String[modelNames.size()]))));
//  }
//

  @EventSubscriber
  public void observePlayerJoinEvent(GameJoinEvent event) {
    GameSession session = new GameSession(event.getGameToken().getCreatedByUser(), event.getGameToken(),
        event.getConnection());
    GameProcessor game = createGame(event.getGameToken().getMap(), session);
    session.setGame(game);
    connections.put(event.getConnection(), session);
    log.info(String.format("Player '<%s>' attempt to join game '<%s>'", event.getGameToken()
        .getCreatedByUser().getUsername(), game.getTopic()));
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

  private void removePlayer(GameSession session, WebSocketConnection connection) {
    ProcessorHandler handler = processors.get(session);
    if (handler != null && handler.isStarted()) {
      handler.stop();
    }
    GameProcessor game = session.getGame();
    game.removePlayer(connection);
    connections.remove(connection);
    processors.remove(session);
    // TODO: Current we shutdown the games without users, later they should go on...
    if (game.getSessions().size() == 0) {
      game.stop();
      games.remove(game);
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
