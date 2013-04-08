package game.network.handler;

import game.GameSession;
import game.GamesHandler;
import game.event.GameLeaveEvent;

import java.util.HashMap;
import java.util.Map;

import models.entity.User;

import org.bushe.swing.event.EventBus;
import org.webbitserver.BaseWebSocketHandler;
import org.webbitserver.WebSocketConnection;

import play.Logger;

/**
 * The websocket handler controls every connection between server and clients.
 * 
 * @author Alexander Wilhelmer
 */
public class WebSocketHandler extends BaseWebSocketHandler {

  private final static Logger.ALogger log = Logger.of(WebSocketHandler.class);

  private static WebSocketHandler instance = new WebSocketHandler();

  private Map<WebSocketConnection, User> authConnections = new HashMap<WebSocketConnection, User>();

  private WebSocketHandler() {
  }

  public static WebSocketHandler getInstance() {
    return instance;
  }

  public Map<WebSocketConnection, User> getAuthConnections() {
    return authConnections;
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
      log.info(String.format("Player <%s> has latency of <%s>", session.getUser().getUsername(), latency));
      session.setLatency(latency);

    }
  }
}