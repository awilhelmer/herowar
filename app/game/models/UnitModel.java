package game.models;

import models.entity.game.Path;
import models.entity.game.Waypoint;

@SuppressWarnings("serial")
public class UnitModel extends BaseModel {

  private long currentHealth = 1000;
  private long maxHealth = 1000;
  private double lastDistance = Double.MAX_VALUE;
  private Path activePath;
  private Waypoint activeWaypoint;
  private TowerModel lastHitTower;
  private boolean endPointReached = false;

  public UnitModel(Long id, Long dbId) {
    super(id, dbId);
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
   * @param tower
   *          The tower to set.
   * @param damage
   *          The damage to set.
   */
  public void hit(TowerModel tower, long damage) {
    if (getCurrentHealth() <= 0) {
      return;
    }
    long newHealth = getCurrentHealth() - damage;
    if (newHealth <= 0) {
      setLastHitTower(tower);
    }
    setCurrentHealth(newHealth > 0 ? newHealth : 0);
  }

  /**
   * Get current health in percentage.
   * 
   * @return long Percentage of health.
   */
  public long getHealthPercentage() {
    double currHealth = getCurrentHealth() < 0 ? 0 : getCurrentHealth();
    return Math.round(currHealth / ((double) getMaxHealth()) * 100);
  }

  /**
   * Is this unit dealth?
   * 
   * @return boolean If this unit is dead.
   */
  public boolean isDeath() {
    return getCurrentHealth() <= 0;
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

  public double getLastDistance() {
    return lastDistance;
  }

  public void setLastDistance(double lastDistance) {
    this.lastDistance = lastDistance;
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

  public TowerModel getLastHitTower() {
    return lastHitTower;
  }

  public void setLastHitTower(TowerModel lastHitTower) {
    this.lastHitTower = lastHitTower;
  }

  public void setEndPointReached(boolean b) {
    endPointReached = b;
  }

  public boolean isEndPointReached() {
    return endPointReached;
  }

}
