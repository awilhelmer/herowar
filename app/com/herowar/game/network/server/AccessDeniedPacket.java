package com.herowar.game.network.server;

import com.herowar.game.network.BasePacket;
import com.herowar.game.network.PacketType;

/**
 * Server send access denied packet.
 * 
 * @author Alexander Wilhelmer
 */
@SuppressWarnings("serial")
public class AccessDeniedPacket extends BasePacket {

	public AccessDeniedPacket() {
		this.type = PacketType.AccessDeniedPacket;
	}

	@Override
	public String toString() {
		return "AccessDeniedPacket [type=" + type + "]";
	}
}
