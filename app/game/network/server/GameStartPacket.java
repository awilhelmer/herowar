package game.network.server;

import game.network.BasePacket;
import game.network.PacketType;

/**
 * The GameStartPacket will be send once the preload phase is over and the game is starting.
 * 
 * @author Sebastian Sachtleben
 */
@SuppressWarnings("serial")
public class GameStartPacket extends BasePacket {

	public GameStartPacket() {
		this.type = PacketType.GameStartPacket;
	}

	@Override
	public String toString() {
		return "GameStartPacket [type=" + type + "]";
	}
}
