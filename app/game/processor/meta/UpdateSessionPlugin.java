package game.processor.meta;

import game.Session;
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
		Iterator<Session> iter = getSessions().iterator();
		while (iter.hasNext()) {
			Session session = iter.next();
			processSession(session, delta, now);
		}
	}

	public abstract void processSession(Session session, double delta, long now);
}
