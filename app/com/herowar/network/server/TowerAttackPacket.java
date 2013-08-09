package com.herowar.network.server;

import com.herowar.network.BasePacket;
import com.herowar.network.PacketType;

/**
 * The TowerAttackPacket will be send from server to client when a tower attacks his target.
 * 
 * @author Sebastian Sachtleben
 */
@SuppressWarnings("serial")
public class TowerAttackPacket extends BasePacket {

	protected long tower;
	protected long damage;

	public TowerAttackPacket(long tower, long damage) {
		super();
		this.type = PacketType.TowerAttackPacket;
		this.tower = tower;
		this.damage = damage;
	}

	public long getTower() {
		return tower;
	}

	public void setTower(long tower) {
		this.tower = tower;
	}

	public long getDamage() {
		return damage;
	}

	public void setDamage(long damage) {
		this.damage = damage;
	}
}
