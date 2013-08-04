package game.network;

import game.network.handler.WebSocketHandler;

import org.webbitserver.WebServer;
import org.webbitserver.WebServers;

import play.Application;
import play.Logger;
import play.Plugin;

/**
 * The {@link WebSocketServer} is a {@link WebServer} instance to handle the web socket connections.
 * <p>
 * This class is registered as Play plugin and starts the {@link WebServer} during application start.
 * </p>
 * 
 * @author Sebastian Sachtleben
 * @see WebServer
 * @see Plugin
 */
public class WebSocketServer extends Plugin {
	private static final Logger.ALogger log = Logger.of(WebSocketServer.class);

	private WebSocketHandler handler;
	private WebServer server;
	private Application app;

	/**
	 * Default constructor will be invoked during startup from play.
	 * 
	 * @param app
	 *          The {@link Application} instance.
	 */
	public WebSocketServer(final Application app) {
		this.app = app;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see play.Plugin#onStart()
	 */
	@Override
	public void onStart() {
		handler = WebSocketHandler.getInstance();
		handler.init();
		server = WebServers.createWebServer(app.configuration().getInt("webSocketServerPort", 9005));
		server.add("/", handler);
		log.info(String.format("Started %s on %s", getClass().getSimpleName(), server.getUri().toString()));
		server.start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see play.Plugin#onStop()
	 */
	@Override
	public void onStop() {
		handler.destroy();
		server.stop();
	}
}
