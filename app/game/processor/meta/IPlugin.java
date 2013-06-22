package game.processor.meta;

import game.GameSession;
import game.processor.GameProcessor;

/**
 * @author Sebastian Sachtleben
 */
public interface IPlugin {

  public void process(double delta, long now);

  public void load();

  public void unload();

  public GameProcessor.State onState();
  
  public void addPlayer(GameSession session);

  public void removePlayer(GameSession session);

}
