package game.processor.plugin;

import game.GameSession;
import game.models.TowerModel;
import game.models.UnitModel;
import game.network.server.TowerAttackPacket;
import game.network.server.TowerTargetPacket;
import game.processor.GameProcessor;
import game.processor.GameProcessor.State;
import game.processor.meta.AbstractPlugin;
import game.processor.meta.IPlugin;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * The TowerUpdatePlugin handles all tower on the map and calculates the current
 * target and handle shots.
 * 
 * @author Sebastian Sachtleben
 */
public class TowerPlugin extends AbstractPlugin implements IPlugin {

  public TowerPlugin(GameProcessor processor) {
    super(processor);
  }

  @Override
  public void process(double delta, long now) {
    Set<UnitModel> units = getProcessor().getUnits();

    Collection<TowerModel> towers = getProcessor().getTowerCache().values();

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
        if (tower.inRange(target) && tower.shoot(target)) {
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
  public void addPlayer(GameSession player) {
    // Empty
  }

  @Override
  public void removePlayer(GameSession player) {
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
