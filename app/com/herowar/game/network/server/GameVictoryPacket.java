package com.herowar.game.network.server;

import com.herowar.game.network.BasePacket;
import com.herowar.game.network.PacketType;

/**
 * Server send GameVictoryPacket on victory.
 * 
 * @author Sebastian Sachtleben
 */
@SuppressWarnings("serial")
public class GameVictoryPacket extends BasePacket {

	public GameVictoryPacket() {
		this.type = PacketType.GameVictoryPacket;
	}

	@Override
	public String toString() {
		return "GameVictoryPacket [type=" + type + "]";
	}
}
