package game.processor.meta;

import game.GameSession;

/**
 * @author Sebastian Sachtleben
 */
public interface IPlugin {

  public void process(double delta, long now);

  public void load();

  public void unload();

  public void addPlayer(GameSession session);

  public void removePlayer(GameSession session);

}
