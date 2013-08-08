package com.herowar.game.network.server;

import com.herowar.game.network.BasePacket;
import com.herowar.game.network.PacketType;

/**
 * The TowerBuildRejectedPacket will be send from server to client when the user requests a tower build but he is not allow to do this.
 * 
 * @author Sebastian Sachtleben
 */
@SuppressWarnings("serial")
public class TowerBuildRejectedPacket extends BasePacket {

	public TowerBuildRejectedPacket() {
		super();
		this.type = PacketType.TowerBuildRejectedPacket;
	}

	@Override
	public String toString() {
		return "TowerBuildRejectedPacket [type=" + type + ", createdTime=" + createdTime + "]";
	}
}
