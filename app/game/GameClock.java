package game;

import java.io.Serializable;

/**
 * Provides delta for game simulator.
 * 
 * @author Alexander Wilhelmer
 * @author Sebastian Sachtleben
 */
@SuppressWarnings("serial")
public class GameClock implements Serializable {

  private Long oldTime = null;
  
  public void reset() {
    oldTime = null;
  }

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
