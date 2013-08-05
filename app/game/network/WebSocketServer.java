package game.network;

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

	public abstract class SettingKeys {
		public static final String START = "websocket.start";
		public static final String PORT = "websocket.port";
	}

	public abstract class SettingDefault {
		public static final boolean START = true;
		public static final int PORT = 9005;
	}

	private Application app;
	private WebServer server;

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
		if (!app.configuration().getBoolean(SettingKeys.START, SettingDefault.START)) {
			log.warn(String.format("Prevent starting of %s because '%s' conf is 'false'", SettingKeys.START, getClass().getSimpleName()));
			return;
		}
		WebServer server = WebServers.createWebServer(app.configuration().getInt(SettingKeys.PORT, SettingDefault.PORT));
		server.add("/", new WebSocketHandler());
		server.start();
		log.info(String.format("Started %s on %s", getClass().getSimpleName(), server.getUri().toString()));
		this.server = server;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see play.Plugin#onStop()
	 */
	@Override
	public void onStop() {
		if (server != null) {
			server.stop();
		}
	}
}
