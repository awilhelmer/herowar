package game.processor.meta;

import play.Logger;

/**
 * Abstract processor class to handle multithreaded game backend.
 * 
 * @author Alexander Wilhelmer
 * @author Sebastian Sachtleben
 */
public abstract class AbstractProcessor implements Runnable {

	private final static Logger.ALogger log = Logger.of(AbstractProcessor.class);

	protected String topicName;
	private Thread thread = null;
	private boolean running = true;

	public AbstractProcessor(String topicName) {
		if (getUpdateTimer() <= 0) {
			log.warn("Deactivated Processor");
			this.running = false;
		}
		this.topicName = topicName;
	}

	@Override
	public void run() {
		if (getUpdateTimer() > 0) {
			while (running) {
				process();
				try {
					Thread.sleep(getUpdateTimer());
				} catch (InterruptedException e) {
					log.error("", e);
				}
			}
		}
	}

	public void start() {
		thread = new Thread(this);
		thread.start();
	}

	public void stop() {
		thread = null;
		running = false;
	}

	public abstract void process();

	public abstract int getUpdateTimer();

	// GETTER && SETTER //

	public String getTopicName() {
		return topicName;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}
}
