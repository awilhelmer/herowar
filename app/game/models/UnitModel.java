package game.models;

import models.entity.game.Path;
import models.entity.game.Waypoint;

@SuppressWarnings("serial")
public class UnitModel extends BaseModel {

  private long currentHealth;
  private long maxHealth;
  private Path activePath;
  private Waypoint activeWaypoint;
  private boolean endPointReached = false;

  public UnitModel(Long id, Long dbId) {
    super(id, dbId);
    currentHealth = 1000;
    maxHealth = 1000;
  }

  /**
   * Rotate to current waypoint.
   * 
   * @param delta
   *          The delta to set
   */
  public void rotateTo(Double delta) {
    super.rotateTo(getActiveWaypoint().getPosition(), delta);
  }

  /**
   * Hit unit with damage.
   * 
   * @param damage
   *          The damage to set.
   */
  public void hit(long damage) {
    setCurrentHealth(getCurrentHealth() - damage);
  }

  // GETTER & SETTER //

  public Path getActivePath() {
    return activePath;
  }

  public long getCurrentHealth() {
    return currentHealth;
  }

  public void setCurrentHealth(long currentHealth) {
    this.currentHealth = currentHealth;
  }

  public long getMaxHealth() {
    return maxHealth;
  }

  public void setMaxHealth(long maxHealth) {
    this.maxHealth = maxHealth;
  }

  public void setActivePath(Path activePath) {
    this.activePath = activePath;
  }

  public Waypoint getActiveWaypoint() {
    return activeWaypoint;
  }

  public void setActiveWaypoint(Waypoint activeWaypoint) {
    this.activeWaypoint = activeWaypoint;
  }

  public void setEndPointReached(boolean b) {
    endPointReached = b;
  }

  public boolean isEndPointReached() {
    return endPointReached;
  }

}
