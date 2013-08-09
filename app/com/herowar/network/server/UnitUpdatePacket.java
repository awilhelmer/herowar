package com.herowar.network.server;

import com.herowar.models.UnitModel;

/**
 * Update informations about a unit in the current running match.
 * 
 * @author Sebastian Sachtleben
 */
@SuppressWarnings("serial")
public class UnitUpdatePacket extends ObjectInPacket {

	protected long health;
	protected long healthMax;
	protected long shield;
	protected long shieldMax;
	protected long waypoint;

	public UnitUpdatePacket(UnitModel unitModel) {
		super(unitModel.getId(), unitModel.getName(), unitModel.getTranslation());
		this.health = unitModel.getCurrentHealth();
		this.healthMax = unitModel.getEntity().getHealth();
		this.shield = unitModel.getCurrentShield();
		this.shieldMax = unitModel.getEntity().getShield();
		this.waypoint = unitModel.getActiveWaypoint().getId();
	}

	public long getHealth() {
		return health;
	}

	public void setHealth(long health) {
		this.health = health;
	}

	public long getHealthMax() {
		return healthMax;
	}

	public void setHealthMax(long healthMax) {
		this.healthMax = healthMax;
	}

	public long getShield() {
		return shield;
	}

	public void setShield(long shield) {
		this.shield = shield;
	}

	public long getShieldMax() {
		return shieldMax;
	}

	public void setShieldMax(long shieldMax) {
		this.shieldMax = shieldMax;
	}

	public long getWaypoint() {
		return waypoint;
	}

	public void setWaypoint(long waypoint) {
		this.waypoint = waypoint;
	}
}
