package com.herowar.game.network.server;

import com.herowar.game.network.BasePacket;
import com.herowar.game.network.PacketType;

/**
 * Server send access granted packet.
 * 
 * @author Alexander Wilhelmer
 */

@SuppressWarnings("serial")
public class AccessGrantedPacket extends BasePacket {

	public AccessGrantedPacket() {
		this.type = PacketType.AccessGrantedPacket;
	}

	@Override
	public String toString() {
		return "AccessGrantedPacket [type=" + type + "]";
	}

}
