package com.herowar.network.server;

import com.herowar.models.TowerRestriction;
import com.herowar.models.entity.game.Vector3;
import com.herowar.network.BasePacket;
import com.herowar.network.PacketType;


/**
 * The TowerAreaRescritionPacket will be send from server to client and tell where tower can be build.
 * 
 * @author Sebastian Sachtleben
 */
@SuppressWarnings("serial")
public class TowerAreaRestrictionPacket extends BasePacket {

	protected Vector3 position;
	protected int radius;

	public TowerAreaRestrictionPacket(TowerRestriction restriction) {
		super();
		this.type = PacketType.TowerAreaRestrictionPacket;
		this.position = restriction.getPosition();
		this.radius = restriction.getRadius();
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	public Vector3 getPosition() {
		return position;
	}

	public void setPosition(Vector3 position) {
		this.position = position;
	}

	@Override
	public String toString() {
		return "TowerAreaRestrictionPacket [type=" + type + ", createdTime=" + createdTime + "]";
	}
}
