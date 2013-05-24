package game.models;

import game.GameSession;

import java.util.Date;
import java.util.Set;

/**
 * The Tower is placed somewhere on the map by any player.
 * 
 * @author Sebastian Sachtleben
 */
@SuppressWarnings("serial")
public class TowerModel extends BaseModel {
  public static final int RANGE = 100;
  public static final int RELOAD = 1000;

  private UnitModel target;
  private GameSession session;
  private Date lastShot;

  public TowerModel(Long id, Long dbId) {
    super(id, dbId);
    lastShot = new Date();
  }

  /**
   * Rotate to current waypoint.
   * 
   * @param target
   *          The target to set
   * @param delta
   *          The delta to set
   */
  public void rotateTo(UnitModel target, Double delta) {
    super.rotateTo(target.getTranslation().clone(), delta);
  }

  /**
   * Check if the tower is allowed to shoot.
   * 
   * @param target
   *          The target to set
   * @return boolean If tower can shoot.
   */
  public boolean shoot(UnitModel target) {
    Date now = new Date();
    // TODO: only shot if towers looks in the proper direction (dunno how to
    // check this yet...)
    if (target != null && inRange(target) && (now.getTime() - TowerModel.RELOAD >= getLastShot().getTime())) {
      setLastShot(now);
      return true;
    }
    return false;
  }

  /**
   * Find a valid target for this tower.
   * 
   * @param units
   *          The units to set
   * @return UnitModel The best target.
   */
  public UnitModel findTarget(Set<UnitModel> units) {
    if (getTarget() != null && !getTarget().isDeath() && inRange(getTarget())) {
      return getTarget();
    }
    for (UnitModel unit : units) {
      if (!unit.isDeath() && inRange(unit)) {
        return unit;
      }
    }
    return null;
  }

  /**
   * Check if target is valid.
   * 
   * @return boolean If target is valid.
   */
  public boolean hasTarget() {
    return target != null ? true : false;
  }

  /**
   * Get distance to target.
   * 
   * @return double The distance to target.
   */
  public double getTargetDistance() {
    return getTarget() != null ? getTranslation().distance(getTarget().getTranslation()) : 0;
  }

  /**
   * Check if UnitModel is in range.
   * 
   * @param model
   *          The model to set.
   * @return boolean If model is in range.
   */
  public boolean inRange(UnitModel model) {
    return getTranslation().distance(model.getTranslation()) <= RANGE ? true : false;
  }

  // GETTER & SETTER

  public UnitModel getTarget() {
    return target;
  }

  public void setTarget(UnitModel target) {
    this.target = target;
  }

  public GameSession getSession() {
    return session;
  }

  public void setSession(GameSession session) {
    this.session = session;
  }

  public Date getLastShot() {
    return lastShot;
  }

  public void setLastShot(Date lastShot) {
    this.lastShot = lastShot;
  }
}
