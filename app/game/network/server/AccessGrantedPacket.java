package game.network.server;

import game.network.BasePacket;
import game.network.PacketType;

/**
 * Server send access granted packet.
 * 
 * @author Alexander Wilhelmer
 */

public class AccessGrantedPacket extends BasePacket {
  private static final long serialVersionUID = 8377557173578070738L;

  public AccessGrantedPacket() {
		this.type = PacketType.AccessGrantedPacket;
	}
	
	@Override
	public String toString() {
		return "AccessGrantedPacket [type=" + type + "]";
	}
	
}
