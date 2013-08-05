package game.network.client;

import game.network.ClientPacket;
import game.network.PacketType;
import game.network.server.GameStartPacket;
import game.processor.GameProcessor;
import game.processor.GameProcessor.Topic;
import play.Logger;
import play.libs.Json;

/**
 * Send from client when preloading is updated. The progress represents the percent of loading and could be a integer value from 0 to 100.
 * 
 * @author Sebastian Sachtleben
 */
@ClientPacket(type = PacketType.ClientPreloadUpdatePacket)
@SuppressWarnings("serial")
public class ClientPreloadUpdatePacket extends BaseClientAuthPacket {
	private static final Logger.ALogger log = Logger.of(ClientPreloadUpdatePacket.class);

	private Integer progress;

	@Override
	public void process() {
		if (GameProcessor.State.PRELOAD.equals(session.getGame().getState())) {
			if (progress == 100) {
				log.info("Send preload complete event to " + session.getGame().getTopicName() + " for "
						+ session.getPlayer().getUser().getUsername());
				session.setPreloading(false);
			}
			session.getGame().publish(Topic.PRELOAD, session.getPlayerId(), progress);
		} else if (progress == 100) {
			session.getConnection().send(Json.toJson(new GameStartPacket()).toString());
			session.getGame().syncronizePlayer(session);
			session.setPreloading(false);
		}
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
