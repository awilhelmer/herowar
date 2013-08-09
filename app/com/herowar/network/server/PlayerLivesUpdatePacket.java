package com.herowar.network.server;

import com.herowar.network.BasePacket;
import com.herowar.network.PacketType;

/**
 * Server sends player stats to client to syncronize lives.
 * 
 * @author Sebastian Sachtleben
 */
@SuppressWarnings("serial")
public class PlayerLivesUpdatePacket extends BasePacket {

	protected Integer lives;

	public PlayerLivesUpdatePacket(Integer lives) {
		super();
		this.type = PacketType.PlayerLivesUpdatePacket;
		this.lives = lives;
	}

	public Integer getLives() {
		return lives;
	}

	public void setLives(Integer lives) {
		this.lives = lives;
	}

	@Override
	public String toString() {
		return "PlayerLivesUpdatePacket [type=" + type + ", createdTime=" + createdTime + ", lives=" + lives + "]";
	}
}