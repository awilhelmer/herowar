package game.network;

import play.Logger;
import play.libs.F.Callback;
import play.libs.F.Callback0;

/**
 * Handles game related web socket connections.
 * 
 * @author Sebastian Sachtleben
 */
public class WebSocket extends play.mvc.WebSocket<String> {
	private static final Logger.ALogger log = Logger.of(WebSocket.class);

	private MessageCallback messageCallback = new MessageCallback();
	private CloseCallback closeCallback = new CloseCallback();

	/*
	 * (non-Javadoc)
	 * 
	 * @see play.mvc.WebSocket#onReady(play.mvc.WebSocket.In, play.mvc.WebSocket.Out)
	 */
	@Override
	public void onReady(play.mvc.WebSocket.In<String> in, play.mvc.WebSocket.Out<String> out) {
		in.onMessage(messageCallback);
		in.onClose(closeCallback);
	}

	public class MessageCallback implements Callback<String> {

		/*
		 * (non-Javadoc)
		 * 
		 * @see play.libs.F.Callback#invoke(java.lang.Object)
		 */
		@Override
		public void invoke(String msg) throws Throwable {
			log.info(String.format("Received: %s", msg));
		}

	}

	public class CloseCallback implements Callback0 {

		/*
		 * (non-Javadoc)
		 * 
		 * @see play.libs.F.Callback0#invoke()
		 */
		@Override
		public void invoke() throws Throwable {
			log.info("Disconnected");
		}

	}

}
