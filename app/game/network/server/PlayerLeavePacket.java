package game.network.server;

import game.network.BasePacket;
import game.network.PacketType;

/**
 * Server sends info that a new player left.
 * 
 * @author Alexander Wilhelmer
 */
public class PlayerLeavePacket extends BasePacket {
  private static final long serialVersionUID = -7908654617988848218L;

  public PlayerLeavePacket() {
		this.type = PacketType.PlayerLeavePacket;
	}

	@Override
	public String toString() {
		return "PlayerLeavePacket [type=" + type + "]";
	}
	
}
