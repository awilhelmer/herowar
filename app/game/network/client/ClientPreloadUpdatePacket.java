package game.network.client;

import game.GameSession;
import game.GamesHandler;
import game.event.PreloadUpdateEvent;
import game.network.BasePacket;
import game.network.InputPacket;
import game.network.handler.PacketHandler;
import game.network.handler.WebSocketHandler;
import game.processor.GameProcessor.Topic;
import game.processor.PlayerProcessor;
import game.processor.ProcessorHandler;
import game.processor.meta.IProcessor;

import java.util.ArrayList;
import java.util.List;

import org.webbitserver.WebSocketConnection;

import play.Logger;

/**
 * Send from client when preloading is updated. The progress represents the
 * percent of loading and could be a integer value from 0 to 100.
 * 
 * @author Sebastian Sachtleben
 */
@SuppressWarnings("serial")
public class ClientPreloadUpdatePacket extends BasePacket implements InputPacket {
	private static final Logger.ALogger log = Logger.of(ClientPreloadUpdatePacket.class);

	private Integer progress;

	@Override
	public void process(PacketHandler packetHandler, WebSocketHandler socketHandler, WebSocketConnection connection) {
		log.info("Process " + this.toString());
		// TODO: we should wait here untill all users finished preloading ...
		GameSession session = GamesHandler.getInstance().getConnections().get(connection);
		if (session == null) {
			// TODO: disconnect user here ...
			log.error("GameSession should not be null");
			return;
		}
		// TODO: this could be end badly if the user send multiple times 100
		// progress...
		if (progress == 100) {
			session.getClock().reset();
			List<IProcessor> list = new ArrayList<IProcessor>();
			PlayerProcessor playerProcessor = new PlayerProcessor(session.getToken().getToken(), connection);
			session.setPlayerProcessor(playerProcessor);
			list.add(playerProcessor);
			ProcessorHandler handler = new ProcessorHandler(list);
			handler.start();
			GamesHandler.getInstance().getProcessors().put(session, handler);
			log.info("Send preload complete event to " + session.getGame().getTopicName() + " for " + session.getUser().getUsername());
		}
		session.getGame().publish(Topic.PRELOAD, new PreloadUpdateEvent(session.getUser().getId(), progress));
	}

	public Integer getProgress() {
		return progress;
	}

	public void setProgress(Integer progress) {
		this.progress = progress;
	}

	@Override
	public String toString() {
		return "ClientPreloadUpdatePacket [type=" + type + ", progress=" + progress + "]";
	}
}
