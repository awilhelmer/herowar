package com.herowar.processor.meta;


import java.util.Iterator;

import com.herowar.network.Connection;
import com.herowar.processor.GameProcessor;

/**
 * The UpdateSessionPlugin iterates over each game session and invoke processSession method which needs to be implemented.
 * 
 * @author Sebastian Sachtleben
 */
public abstract class UpdateSessionPlugin extends AbstractPlugin {

	public UpdateSessionPlugin(GameProcessor processor) {
		super(processor);
	}

	public void process(double delta, long now) {
		Iterator<Connection> iter = connections().iterator();
		while (iter.hasNext()) {
			Connection connection = iter.next();
			processConnection(connection, delta, now);
		}
	}

	public abstract void processConnection(Connection connection, double delta, long now);
}
