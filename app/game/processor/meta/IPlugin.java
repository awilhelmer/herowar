package game.processor.meta;

import game.GameSession;

/**
 * @author Sebastian Sachtleben
 */
public interface IPlugin {

  public void process();
  
  public void load();
  
  public void unload();
  
  public void addPlayer(GameSession player);
  
  public void removePlayer(GameSession player);
  
}
