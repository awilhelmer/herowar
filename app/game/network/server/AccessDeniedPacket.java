package game.network.server;

import game.network.BasePacket;
import game.network.PacketType;

/**
 * Server send access denied packet.
 * 
 * @author Alexander Wilhelmer
 */
public class AccessDeniedPacket extends BasePacket {
  private static final long serialVersionUID = -6495912566763954467L;

  public AccessDeniedPacket() {
		this.type = PacketType.AccessDeniedPacket;
	}
	
	@Override
	public String toString() {
		return "AccessDeniedPacket [type=" + type + "]";
	}
	
}
