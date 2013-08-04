package game.network.handler;

import game.GameSession;
import game.GamesHandler;
import game.Sessions;
import game.event.GameLeaveEvent;

import java.util.HashMap;
import java.util.Map;

import models.entity.game.Player;

import org.webbitserver.BaseWebSocketHandler;
import org.webbitserver.WebSocketConnection;

import play.Logger;

import com.ssachtleben.play.plugin.event.Events;

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
		GamesHandler.getInstance();
		log.info("WebSocketHandler started");
	}

	public void destroy() {
		GamesHandler.getInstance().stop();
		log.info("WebSocketHandler stopped");
	}

	@Override
	public void onOpen(final WebSocketConnection connection) {
		log.debug("New connection " + connection.httpRequest().id() + " opend - waiting for auth packet");
	}

	@Override
	public void onClose(final WebSocketConnection connection) {
		if (authConnections.containsKey(connection)) {
			log.info("Auth connection " + connection.httpRequest().id() + " closed");
			authConnections.remove(connection);
			Events.instance().publish(GamesHandler.EVENT_TOPIC, new GameLeaveEvent(connection));
		}
	}

	@Override
	public void onMessage(final WebSocketConnection connection, final String msg) throws Throwable {
		PacketHandler.getInstance().handle(this, connection, msg);
	}

	@Override
	public void onPing(final WebSocketConnection connection, final byte[] msg) throws Throwable {
		super.onPing(connection, msg);
	}

	@Override
	public void onPong(final WebSocketConnection connection, final byte[] msg) throws Throwable {
		super.onPong(connection, msg);
		GameSession session = Sessions.get(connection);
		if (session != null) {
			Long latency = Long.parseLong(new String(msg));
			latency = (System.currentTimeMillis() - latency) / 2;
			log.info(String.format("Player <%s> has latency of <%s>", session.getPlayer().getUser().getUsername(), latency));
			session.setLatency(latency);

		}
	}
}
