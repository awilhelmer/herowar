package com.herowar.game.network.server;

import com.herowar.game.network.PacketType;

/**
 * Server sends first time info about the player stats.
 * 
 * @author Sebastian Sachtleben
 */
@SuppressWarnings("serial")
public class PlayerStatsInitPacket extends PlayerStatsUpdatePacket {

	protected Integer goldPerTick;

	public PlayerStatsInitPacket(Long lives, Long gold, Integer goldPerTick) {
		super(0L, lives, gold, 0, lives.intValue(), gold.intValue());
		this.type = PacketType.PlayerStatsInitPacket;
		this.goldPerTick = goldPerTick;
	}

	public Integer getGoldPerTick() {
		return goldPerTick;
	}

	public void setGoldPerTick(Integer goldPerTick) {
		this.goldPerTick = goldPerTick;
	}

	@Override
	public String toString() {
		return "PlayerStatsInitPacket [type=" + type + ", createdTime=" + createdTime + ", lives=" + lives + ", gold=" + gold
				+ ", goldPerTick=" + goldPerTick + "]";
	}
}
