package game.processor.plugin;

import game.GameSession;
import game.processor.GameProcessor;
import game.processor.meta.AbstractPlugin;
import game.processor.meta.IPlugin;

/**
 * The WaveUpdatePlugin controls the wave behaviors and update all units.
 * 
 * @author Sebastian Sachtleben
 */
public class WaveUpdatePlugin extends AbstractPlugin implements IPlugin {

  public WaveUpdatePlugin(GameProcessor processor) {
    super(processor);
  }

  @Override
  public void process() {
    // TODO Auto-generated method stub
  }

  @Override
  public void addPlayer(GameSession player) {
    // TODO Auto-generated method stub
  }

  @Override
  public void removePlayer(GameSession player) {
    // TODO Auto-generated method stub
  }

  @Override
  public String toString() {
    return "WaveUpdatePlugin";
  }
}
