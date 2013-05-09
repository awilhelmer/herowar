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
 * @author Sebastian Sachtleben
 */
public abstract class AbstractProcessor implements Runnable {
  
  private final static Logger.ALogger log = Logger.of(AbstractProcessor.class);

  protected String topicName;
  private Thread thread = null;
  private boolean running = true;

  private List<EventTopicSubscriber<? extends Object>> listeners = new ArrayList<EventTopicSubscriber<? extends Object>>();

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
    removeListeners();
  }

  public void addListener(EventTopicSubscriber<? extends Object> listener) {
    EventBus.subscribe(topicName, listener);
    listeners.add(listener);
  }
  
  public void removeListener(EventTopicSubscriber<? extends Object> listener) {
    EventBus.unsubscribe(topicName, listener);
    listeners.remove(listener);
  }
  
  public void removeListeners() {
    for (EventTopicSubscriber<? extends Object> listener : listeners) {
      EventBus.unsubscribe(topicName, listener);
      listeners.remove(listener);
    }
  }
  
  public void publish(Object o) {
    EventBus.publish(topicName, o);
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
