package game.processor.meta;

import game.Session;
import game.processor.GameProcessor;

/**
 * @author Sebastian Sachtleben
 */
public interface IPlugin {

	public void process(double delta, long now);

	public void load();

	public void unload();

	public GameProcessor.State onState();

	public void addPlayer(Session session);

	public void removePlayer(Session session);

}
