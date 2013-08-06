package game.processor.meta;

import game.network.Connection;
import game.processor.GameProcessor;

import java.util.Iterator;

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
