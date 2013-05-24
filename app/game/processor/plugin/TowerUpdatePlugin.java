package game.processor.plugin;

import game.GameSession;
import game.models.TowerModel;
import game.models.UnitModel;
import game.network.server.ObjectInPacket;
import game.network.server.TowerTargetPacket;
import game.processor.GameProcessor;
import game.processor.meta.AbstractPlugin;
import game.processor.meta.IPlugin;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import play.Logger;

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
      if (target != null && target != tower.getTarget()) {
        tower.setTarget(target);
        //log.info("Tower " + tower + " has new target " + tower.getTarget() + " (" + tower.getTargetDistance() + ")");
        TowerTargetPacket packet = new TowerTargetPacket(tower.getId(), target.getId());
        broadcast(packet);
      }
      if (tower.hasTarget()) {
        tower.rotateTo(delta);
        if (tower.shoot()) {
          log.info("Tower " + tower + " shoots");
        }
      }
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
