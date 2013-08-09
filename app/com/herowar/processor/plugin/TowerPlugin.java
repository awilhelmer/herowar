package com.herowar.processor.plugin;


import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import com.herowar.models.TowerModel;
import com.herowar.models.UnitModel;
import com.herowar.network.Connection;
import com.herowar.network.server.TowerAttackPacket;
import com.herowar.network.server.TowerTargetPacket;
import com.herowar.processor.GameProcessor;
import com.herowar.processor.GameProcessor.State;
import com.herowar.processor.meta.AbstractPlugin;
import com.herowar.processor.meta.IPlugin;

/**
 * The TowerUpdatePlugin handles all tower on the map and calculates the current target and handle shots.
 * 
 * @author Sebastian Sachtleben
 */
public class TowerPlugin extends AbstractPlugin implements IPlugin {

	public TowerPlugin(GameProcessor processor) {
		super(processor);
	}

	@Override
	public void process(double delta, long now) {
		Set<UnitModel> units = game().getUnits();

		Collection<TowerModel> towers = game().getTowerCache().values();

		Iterator<TowerModel> iter = towers.iterator();
		while (iter.hasNext()) {
			TowerModel tower = iter.next();
			UnitModel target = null;
			synchronized (units) {
				target = tower.findTarget(units);
			}
			if (target != null) {
				if (target != tower.getTarget()) {
					TowerTargetPacket packet = new TowerTargetPacket(tower.getId(), target.getId());
					broadcast(packet);
				}
				tower.rotateTo(target, delta);
				if (tower.shoot(target)) {
					long damage = tower.calculateDamage(target);
					target.hit(tower, damage);
					TowerAttackPacket packet = new TowerAttackPacket(tower.getId(), damage);
					broadcast(packet);
				}
			}
			tower.setTarget(target);
		}
	}

	@Override
	public void add(Connection connection) {
		// Empty
	}

	@Override
	public void remove(Connection connection) {
		// Empty
	}

	@Override
	public State onState() {
		return State.GAME;
	}

	@Override
	public String toString() {
		return "TowerPlugin";
	}
}
