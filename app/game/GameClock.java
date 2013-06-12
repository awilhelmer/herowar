package game;

import java.io.Serializable;

/**
 * Provides delta for our game simulator. Each getDelta call will reset the time
 * calculation, so make sure not to call getDelta multiple times during one
 * plugin process step.
 * 
 * @author Alexander Wilhelmer
 * @author Sebastian Sachtleben
 */
@SuppressWarnings("serial")
public class GameClock implements Serializable {

  private long lastTime;
  private long currentTime;

  /**
   * Create a new game clock instances and start the time calculation.
   */
  public GameClock() {
    reset();
  }

  /**
   * Reset the time calculation.
   */
  public void reset() {
    lastTime = System.currentTimeMillis();
  }

  /**
   * Get the current time difference since the last getDelta() call. Attension:
   * Don't call this method multiple times during a plugin process step.
   * 
   * @return Double The delta.
   */
  public double getDelta() {
    double diff = 0d;
    currentTime = System.currentTimeMillis();
    diff = 0.001 * (currentTime - lastTime);
    lastTime = currentTime;
    return diff;
  }

  /**
   * Return the timestamp of the last getDelta() call.
   * 
   * @return Long The current timestamp.
   */
  public long getCurrentTime() {
    return currentTime;
  }
}
