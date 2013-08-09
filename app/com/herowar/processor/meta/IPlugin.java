package com.herowar.processor.meta;

import com.herowar.network.Connection;
import com.herowar.processor.GameProcessor;

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
