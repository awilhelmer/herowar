package com.herowar.game.network;


import org.codehaus.jackson.JsonNode;

import com.herowar.game.network.server.AccessDeniedPacket;
import com.herowar.game.network.server.ServerInitPacket;
import com.herowar.game.processor.GameProcessor;
import com.herowar.models.entity.User;

import play.Logger;
import play.libs.F.Callback;
import play.libs.F.Callback0;
import play.mvc.WebSocket;

/**
 * Handles interacting between client and server during game phase.
 * 
 * @author Sebastian Sachtleben
 */
public class Connection extends WebSocket<JsonNode> {
	private static final Logger.ALogger log = Logger.of(Connection.class);

	private Long id = -1L;
	private User user;
	private String address;
	private GameProcessor game;
	private Connection instance;

	private boolean ready = false;
	private boolean preloading = true;
	private WebSocket.Out<JsonNode> out;

	/**
	 * Create new {@link Connection}.
	 * 
	 * @param address
	 *          The address to set.
	 * @param user
	 *          The {@link User} to set.
	 */
	public Connection(final String address, final User user) {
		this.address = address;
		this.user = user;
		if (user != null) {
			this.id = user.getId();
		}
		this.instance = this;
	}

	/**
	 * @return the instance
	 */
	private Connection instance() {
		return instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see play.mvc.WebSocket#onReady(play.mvc.WebSocket.In, play.mvc.WebSocket.Out)
	 */
	@Override
	public void onReady(final WebSocket.In<JsonNode> in, final WebSocket.Out<JsonNode> out) {
		this.out = out;
		this.ready = true;

		// Check if user is logged in
		if (user == null) {
			log.warn(String.format("Detected unauthorized: %s", instance()));
			send(new AccessDeniedPacket());
			close();
			return;
		}

		// Handle on message
		in.onMessage(new Callback<JsonNode>() {

			@Override
			public void invoke(JsonNode data) throws Throwable {
				log.info(String.format("Received: [connection=%s, data=%s]", instance(), data));
				Packets.handle(instance, data);
			}

		});

		// Handle closing connection
		in.onClose(new Callback0() {

			/*
			 * (non-Javadoc)
			 * 
			 * @see play.libs.F.Callback0#invoke()
			 */
			@Override
			public void invoke() throws Throwable {
				Connections.remove(user.getId());
				log.info(String.format("Disconnected: %s (total=%d)", instance(), Connections.size()));
			}

		});

		// Save connection and send server init packet
		Connections.add(user.getId(), instance);
		log.info(String.format("Connected: %s (total=%d)", user, Connections.size()));
		send(new ServerInitPacket());

	}

	/**
	 * Sends a {@link BasePacket} object to client.
	 * 
	 * @param packet
	 *          The {@link BasePacket} to send.
	 */
	public void send(BasePacket packet) {
		out().write(packet.toJson());
	}

	public void close() {
		out().close();
	}

	/**
	 * @return the out
	 */
	public WebSocket.Out<JsonNode> out() {
		return out;
	}

	/**
	 * @return the id
	 */
	public Long id() {
		return id;
	}

	/**
	 * @return the address
	 */
	public String address() {
		return address;
	}

	/**
	 * @return the ready
	 */
	public boolean ready() {
		return ready;
	}

	/**
	 * @return the user
	 */
	public User user() {
		return user;
	}

	/**
	 * @return the game
	 */
	public GameProcessor game() {
		return game;
	}

	/**
	 * @param game
	 *          the game to set
	 */
	public void game(GameProcessor game) {
		this.game = game;
	}

	/**
	 * @return the ready
	 */
	public boolean preloading() {
		return preloading;
	}

	/**
	 * @param preloading
	 *          the preloading to set
	 */
	public void preloading(boolean preloading) {
		this.preloading = preloading;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Connection [address=" + address + ", user=" + user + ", ready=" + ready + "]";
	}
}
