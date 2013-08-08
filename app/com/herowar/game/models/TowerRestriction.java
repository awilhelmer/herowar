package com.herowar.game.models;

import com.herowar.models.entity.game.Vector3;

public class TowerRestriction {

	private Vector3 position;
	private int radius;

	public TowerRestriction() {
		// empty
	}

	public TowerRestriction(Vector3 position, int radius) {
		this.position = position;
		this.radius = radius;
	}

	public Vector3 getPosition() {
		return position;
	}

	public void setPosition(Vector3 position) {
		this.position = position;
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}
}
