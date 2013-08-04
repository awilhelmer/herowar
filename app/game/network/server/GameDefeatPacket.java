package game.network.server;

import game.network.BasePacket;
import game.network.PacketType;

/**
 * Server send GameDefeatPacket on defeat.
 * 
 * @author Sebastian Sachtleben
 */
@SuppressWarnings("serial")
public class GameDefeatPacket extends BasePacket {

	public GameDefeatPacket() {
		this.type = PacketType.GameDefeatPacket;
	}

	@Override
	public String toString() {
		return "GameDefeatPacket [type=" + type + "]";
	}
}
