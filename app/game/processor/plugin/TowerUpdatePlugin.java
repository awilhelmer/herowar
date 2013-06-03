package game.processor.plugin;

import game.GameSession;
import game.models.TowerModel;
import game.models.UnitModel;
import game.network.server.TowerAttackPacket;
import game.network.server.TowerTargetPacket;
import game.processor.GameProcessor;
import game.processor.meta.AbstractPlugin;
import game.processor.meta.IPlugin;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import play.Logger;

import com.ardor3d.math.MathUtils;

/**
 * The TowerUpdatePlugin handles all tower on the map and calculates the current
 * target and handle shots.
 * 
 * @author Sebastian Sachtleben
 */
public class TowerUpdatePlugin extends AbstractPlugin implements IPlugin {
  private final static Logger.ALogger log = Logger.of(TowerUpdatePlugin.class);

  public TowerUpdatePlugin(GameProcessor processor) {
    super(processor);
  }

  @Override
  public void process(Double delta) {
    Set<UnitModel> units = getProcessor().getUnits();
    Collection<TowerModel> towers = getProcessor().getTowerCache().values();
    Iterator<TowerModel> iter = towers.iterator();
    while (iter.hasNext()) {
      TowerModel tower = iter.next();
      UnitModel target = tower.findTarget(units);
      if (target != null) {
        if (target != tower.getTarget()) {
          TowerTargetPacket packet = new TowerTargetPacket(tower.getId(), target.getId());
          broadcast(packet);
        }
        tower.rotateTo(target, delta);
        if (tower.shoot(target)) {
          long damage = Math.round(tower.getEntity().getDamageMin() + ((tower.getEntity().getDamageMax() - tower.getEntity().getDamageMin()) * MathUtils.nextRandomFloat()));
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
}
