package game;

import game.processor.GameProcessor;
import game.processor.ProcessorHandler;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

  private ConcurrentHashMap<Integer, List<GameProcessor>> games = new ConcurrentHashMap<Integer, List<GameProcessor>>();
  private ConcurrentHashMap<WebSocketConnection, GameSession> connections = new ConcurrentHashMap<WebSocketConnection, GameSession>();
  private ConcurrentHashMap<GameSession, ProcessorHandler> processors = new ConcurrentHashMap<GameSession, ProcessorHandler>();

  private long gameId = 1;

  private static GamesHandler instance = new GamesHandler();

  public static GamesHandler getInstance() {
    return instance;
  }

  private GamesHandler() {
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
//  /**
//   * Observes player leave event and remove player from game.
//   * 
//   * @param event
//   */
//  @EventSubscriber
//  public void observePlayerLeaveEvent(GameLeaveEvent event) {
//    log.info("Remove player with connection " + event.getConnection().httpRequest().id());
//    if (!connections.containsKey(event.getConnection())) {
//      log.error("Couldn't find connection " + event.getConnection().httpRequest().id());
//      return;
//    }
//    GameSession player = connections.get(event.getConnection());
//    if (player != null) {
//      removePlayer(player, event.getConnection());
//    }
//  }
//
//  private void removePlayer(GameSession session, WebSocketConnection connection) {
//    ProcessorHandler handler = processors.get(session);
//    if (handler != null && handler.isStarted()) {
//      handler.stop();
//    }
//    GameProcessor game = session.getGame();
//    game.removePlayer(connection);
//    if (game.getSessions().size() == 0) {
//      log.info("Removing game " + game.getTopic() + " due 0 players left");
//      Iterator<GameProcessor> iter = games.get(game.getEpisode().getId()).iterator();
//      while (iter.hasNext()) {
//        GameProcessor curGame = iter.next();
//        if (curGame.getSessions().size() == 0) {
//          curGame.stop();
//          iter.remove();
//        }
//      }
//    }
//    connections.remove(connection);
//    processors.remove(session);
//  }

  public void stop() {
    for (ProcessorHandler handler : processors.values()) {
      handler.stop();
    }
    connections.clear();
    processors.clear();
    games.clear();
  }

  public Map<Integer, List<GameProcessor>> getGames() {
    return games;
  }

  public Map<WebSocketConnection, GameSession> getConnections() {
    return connections;
  }

  public ConcurrentHashMap<GameSession, ProcessorHandler> getProcessors() {
    return processors;
  }
}
