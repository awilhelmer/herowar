package game;

import java.io.Serializable;
/**
 * Provides delta for game simulator.
 * 
 * @author Alexander Wilhelmer
 */
public class GameClock implements Serializable {

  private static final long serialVersionUID = -5256345395907707410L;

  private Long oldTime = null;

  /**
   * Create new game clock instance.
   */
  public GameClock() {
  }

  /**
   * Get delta since last check. Do not call this multiple times.
   * 
   * @return delta double value
   */
  public Double getDelta() {
    Double diff = 0d;
    Long newTime = System.currentTimeMillis();
    if (this.oldTime == null) {
      this.oldTime = newTime;
    }
    diff = 0.001 * (newTime - this.oldTime);
    this.oldTime = newTime;
    return diff;
  }

}
