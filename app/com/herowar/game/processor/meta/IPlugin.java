package com.herowar.game.processor.meta;

import com.herowar.game.network.Connection;
import com.herowar.game.processor.GameProcessor;

/**
 * @author Sebastian Sachtleben
 */
public interface IPlugin {

	public void process(double delta, long now);

	public void load();

	public void unload();

	public GameProcessor.State onState();

	public void add(Connection connection);

	public void remove(Connection connection);

}
