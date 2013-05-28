package game.network;

import game.network.handler.WebSocketHandler;

import java.io.Serializable;

import org.webbitserver.WebServer;
import org.webbitserver.WebServers;

import play.Logger;

/**
 * @author Sebastian Sachtleben
 */
@SuppressWarnings("serial")
public class GameServer implements Serializable {

  public static final int PORT = 9005;
  
  private static final Logger.ALogger log = Logger.of(GameServer.class);
  
  private static GameServer instance = new GameServer();
  
  private WebSocketHandler webSocketHandler = WebSocketHandler.getInstance();
  
  private WebServer webServer;
  
  private GameServer() {
  }
  
  public static GameServer getInstance() {
    return instance;
  }
  
  public void start() {
    log.info("Create socket server on port " + PORT);
    webSocketHandler.init();
    webServer = WebServers.createWebServer(PORT);
    webServer.add("/", webSocketHandler);
    webServer.start();
  }

  public void shutdown() {
    webSocketHandler.destroy();
    webServer.stop();
  }
  
}
