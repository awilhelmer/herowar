package game.models;

import models.entity.game.Path;
import models.entity.game.Unit;
import models.entity.game.UnitType;
import models.entity.game.Waypoint;

/**
 * @author Sebastian Sachtleben
 */
@SuppressWarnings("serial")
public class UnitModel extends BaseModel<Unit> {

  private Unit entity;
  private UnitType type;
  private long currentHealth;
  private long maxHealth;
  private long currentShield;
  private long maxShield;
  private double lastDistance = Double.MAX_VALUE;
  private Path activePath;
  private Waypoint activeWaypoint;
  private TowerModel lastHitTower;
  private boolean endPointReached = false;

  public UnitModel(Long id, Long dbId, Unit entity) {
    super(id, dbId, entity);
    this.type = entity.getType();
    this.currentHealth = entity.getHealth();
    this.maxHealth = entity.getHealth();
    this.currentShield = entity.getShield();
    this.maxShield = entity.getShield();
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
    if (getCurrentShield() > 0) {
      long newShield = getCurrentShield() - damage;
      if (newShield >= 0) {
        setCurrentShield(newShield);
        damage = 0;
      } else {
        damage -= getCurrentShield();
        setCurrentShield(0);
      }
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

  public Unit getEntity() {
    return entity;
  }

  public void setEntity(Unit entity) {
    this.entity = entity;
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

  public long getCurrentShield() {
    return currentShield;
  }

  public void setCurrentShield(long currentShield) {
    this.currentShield = currentShield;
  }

  public long getMaxShield() {
    return maxShield;
  }

  public void setMaxShield(long maxShield) {
    this.maxShield = maxShield;
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

  public UnitType getType() {
    return type;
  }

  public void setType(UnitType type) {
    this.type = type;
  }

}
