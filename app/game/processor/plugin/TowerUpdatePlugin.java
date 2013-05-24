package game.processor.plugin;

import game.GameSession;
import game.processor.GameProcessor;
import game.processor.meta.AbstractPlugin;
import game.processor.meta.IPlugin;
import game.processor.models.Tower;

import java.util.Collection;
import java.util.Iterator;

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
    Collection<Tower> towers = getProcessor().getTowerCache().values();
    Iterator<Tower> iter = towers.iterator();
    while (iter.hasNext()) {
      Tower tower = iter.next();
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
