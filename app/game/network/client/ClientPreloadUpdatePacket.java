package game.network.client;

import game.network.ClientPacket;
import game.network.PacketType;

/**
 * Send from client when preloading is updated. The progress represents the percent of loading and could be a integer value from 0 to 100.
 * 
 * @author Sebastian Sachtleben
 */
@ClientPacket(type = PacketType.ClientPreloadUpdatePacket)
@SuppressWarnings("serial")
public class ClientPreloadUpdatePacket extends BaseClientAuthPacket {

	private Integer progress;

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
