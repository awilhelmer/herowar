package game.network.handler;

import game.GameSession;
import game.GamesHandler;
import game.event.GameLeaveEvent;

import java.util.HashMap;
import java.util.Map;

import models.entity.User;
import models.entity.game.Player;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventServiceLocator;
import org.bushe.swing.event.ThreadSafeEventService;
import org.webbitserver.BaseWebSocketHandler;
import org.webbitserver.WebSocketConnection;

import play.Logger;

/**
 * The websocket handler controls every connection between server and clients.
 * 
 * @author Alexander Wilhelmer
 * @author Sebastian Sachtleben
 */
public class WebSocketHandler extends BaseWebSocketHandler {

  private final static Logger.ALogger log = Logger.of(WebSocketHandler.class);

  private static WebSocketHandler instance = new WebSocketHandler();

  private Map<WebSocketConnection, Player> authConnections = new HashMap<WebSocketConnection, Player>();

  private WebSocketHandler() {
  }

  public static WebSocketHandler getInstance() {
    return instance;
  }

  public Map<WebSocketConnection, Player> getAuthConnections() {
    return authConnections;
  }

  public void init() {
    System.setProperty(EventServiceLocator.SERVICE_NAME_EVENT_BUS, ThreadSafeEventService.class.getName());
    GamesHandler.getInstance();
    log.info("WebSocketHandler started");
  }

  public void destroy() {
    GamesHandler.getInstance().stop();
    log.info("WebSocketHandler stopped");
  }

  @Override
  public void onOpen(WebSocketConnection connection) {
    log.debug("New connection " + connection.httpRequest().id() + " opend - waiting for auth packet");
  }

  @Override
  public void onClose(WebSocketConnection connection) {
    if (authConnections.containsKey(connection)) {
      log.info("Auth connection " + connection.httpRequest().id() + " closed");
      authConnections.remove(connection);
      EventBus.publish(new GameLeaveEvent(connection));
    }
  }

  @Override
  public void onMessage(WebSocketConnection connection, String msg) throws Throwable {
    PacketHandler.getInstance().handle(this, connection, msg);
  }

  @Override
  public void onPing(WebSocketConnection connection, byte[] msg) throws Throwable {
    super.onPing(connection, msg);
  }

  @Override
  public void onPong(WebSocketConnection connection, byte[] msg) throws Throwable {
    super.onPong(connection, msg);
    GameSession session = GamesHandler.getInstance().getConnections().get(connection);
    if (session != null) {
      Long latency = Long.parseLong(new String(msg));
      latency = (System.currentTimeMillis() - latency) / 2;
      log.info(String.format("Player <%s> has latency of <%s>", session.getPlayer().getUser().getUsername(), latency));
      session.setLatency(latency);

    }
  }
}
