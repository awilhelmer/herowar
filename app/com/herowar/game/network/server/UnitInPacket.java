package com.herowar.game.network.server;

import com.herowar.game.models.UnitModel;
import com.herowar.game.network.PacketType;
import com.herowar.models.entity.game.Vector3;


/**
 * Spawn a new unit in the current running match.
 * 
 * @author Sebastian Sachtleben
 */
@SuppressWarnings("serial")
public class UnitInPacket extends UnitUpdatePacket {

	protected int utype;
	protected int moveSpeed;
	protected double scaleGlow;
	protected boolean burning;
	protected boolean explode;
	protected Vector3 rotation;
	protected long path;

	public UnitInPacket(UnitModel unitModel) {
		super(unitModel);
		this.type = PacketType.UnitInPacket;
		this.utype = unitModel.getType().ordinal();
		this.moveSpeed = unitModel.getEntity().getMoveSpeed();
		this.scaleGlow = unitModel.getEntity().getScaleGlow();
		this.burning = unitModel.getEntity().getBurning();
		this.explode = unitModel.getEntity().getExplode();
		this.rotation = unitModel.getEntity().getRotation();
		this.path = unitModel.getActivePath().getId();
	}

	public int getUtype() {
		return utype;
	}

	public void setUtype(int utype) {
		this.utype = utype;
	}

	public int getMoveSpeed() {
		return moveSpeed;
	}

	public void setMoveSpeed(int moveSpeed) {
		this.moveSpeed = moveSpeed;
	}

	public double getScaleGlow() {
		return scaleGlow;
	}

	public void setScaleGlow(double scaleGlow) {
		this.scaleGlow = scaleGlow;
	}

	public boolean isBurning() {
		return burning;
	}

	public void setBurning(boolean burning) {
		this.burning = burning;
	}

	public boolean isExplode() {
		return explode;
	}

	public void setExplode(boolean explode) {
		this.explode = explode;
	}

	public Vector3 getRotation() {
		return rotation;
	}

	public void setRotation(Vector3 rotation) {
		this.rotation = rotation;
	}

	public long getPath() {
		return path;
	}

	public void setPath(long path) {
		this.path = path;
	}
}
