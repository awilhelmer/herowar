package game.network.handler;

import game.EventKeys;
import game.Session;
import game.Sessions;
import game.network.Connections;

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.webbitserver.BaseWebSocketHandler#onOpen(org.webbitserver.WebSocketConnection)
	 */
	@Override
	public void onOpen(final WebSocketConnection connection) {
		log.debug("New connection " + connection.httpRequest().id() + " - waiting for auth packet");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.webbitserver.BaseWebSocketHandler#onClose(org.webbitserver.WebSocketConnection)
	 */
	@Override
	public void onClose(final WebSocketConnection connection) {
		if (Connections.contains(connection)) {
			log.debug("Connection " + connection.httpRequest().id() + " closed");
			Events.instance().publish(EventKeys.PLAYER_LEAVE, connection);
			Connections.remove(connection);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.webbitserver.BaseWebSocketHandler#onMessage(org.webbitserver.WebSocketConnection, java.lang.String)
	 */
	@Override
	public void onMessage(final WebSocketConnection connection, final String msg) throws Throwable {
		PacketHandler.getInstance().handle(this, connection, msg);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.webbitserver.BaseWebSocketHandler#onPing(org.webbitserver.WebSocketConnection, byte[])
	 */
	@Override
	public void onPing(final WebSocketConnection connection, final byte[] msg) throws Throwable {
		super.onPing(connection, msg);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.webbitserver.BaseWebSocketHandler#onPong(org.webbitserver.WebSocketConnection, byte[])
	 */
	@Override
	public void onPong(final WebSocketConnection connection, final byte[] msg) throws Throwable {
		super.onPong(connection, msg);
		Session session = Sessions.get(connection);
		if (session != null) {
			Long latency = Long.parseLong(new String(msg));
			latency = (System.currentTimeMillis() - latency) / 2;
			log.info(String.format("Player <%s> has latency of <%s>", session.getPlayer().getUser().getUsername(), latency));
			session.setLatency(latency);

		}
	}
}
