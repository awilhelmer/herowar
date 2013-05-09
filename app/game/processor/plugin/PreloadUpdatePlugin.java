package game.processor.plugin;

import game.GameSession;
import game.processor.GameProcessor;
import game.processor.meta.AbstractPlugin;
import game.processor.meta.IPlugin;

/**
 * The PreloadUpdatePlugin sends informations about the preload state of every connected player.
 * 
 * @author Sebastian Sachtleben
 */
public class PreloadUpdatePlugin extends AbstractPlugin implements IPlugin {
  
  public PreloadUpdatePlugin(GameProcessor processor) {
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
    return "PreloadUpdatePlugin";
  }
}
