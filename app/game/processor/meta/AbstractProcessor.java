package game.processor.meta;

import java.util.ArrayList;
import java.util.List;

import org.bushe.swing.event.EventBus;
import org.bushe.swing.event.EventTopicSubscriber;

import play.Logger;

/**
 * Abstract processor class to handle multithreaded game backend.
 * 
 * @author Alexander Wilhelmer
 */
public abstract class AbstractProcessor implements Runnable {
  private final static Logger.ALogger log = Logger.of(AbstractProcessor.class);

  protected String topic;

  private List<EventTopicSubscriber<? extends Object>> tokenListeners = new ArrayList<EventTopicSubscriber<? extends Object>>();
  private Thread thread = null;
  private boolean running = true;

  public AbstractProcessor(String topic) {
    if (getUpdateTimer() <= 0) {
      log.warn("Deactivated Processor");
      this.running = false;
    }
    this.topic = topic;
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
    for (EventTopicSubscriber<? extends Object> listener : tokenListeners) {
      EventBus.unsubscribe(topic, listener);
    }
  }

  public void addTokenListener(EventTopicSubscriber<? extends Object> listener) {
    EventBus.subscribe(topic, listener);
    tokenListeners.add(listener);
  }

  public abstract void process();

  public abstract int getUpdateTimer();

  public boolean isRunning() {
    return running;
  }

  public void setRunning(boolean running) {
    this.running = running;
  }

  public String getTopic() {
    return topic;
  }

  public void setTopic(String topic) {
    this.topic = topic;
  }

}
