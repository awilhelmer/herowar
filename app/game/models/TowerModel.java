package game.models;

import game.GameSession;

import java.util.Date;
import java.util.Set;

import models.entity.game.Tower;

/**
 * The Tower is placed somewhere on the map by any player.
 * 
 * @author Sebastian Sachtleben
 */
@SuppressWarnings("serial")
public class TowerModel extends BaseModel<Tower> {

  private UnitModel target;
  private GameSession session;
  private Date lastShot;

  public TowerModel(Long id, Long dbId, Tower entity) {
    super(id, dbId, entity);
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
    if (target != null && inRange(target) && (now.getTime() - getEntity().getReload() >= getLastShot().getTime())) {
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
    UnitModel optimalUnit = null;
    double shortestDistance = Double.MAX_VALUE;
    for (UnitModel unit : units) {
      if (!unit.isDeath() && inViewRange(unit)) {
        double distance = distance(unit);
        if (distance < shortestDistance) {
          optimalUnit = unit;
          shortestDistance = distance;
        }
      }
    }
    return optimalUnit;
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
    return getTranslation().distance(model.getTranslation()) <= getEntity().getCoverage() ? true : false;
  }

  /**
   * Check if UnitModel is in view range.
   * 
   * @param model
   *          The model to set.
   * @return boolean If model is in view range.
   */
  public boolean inViewRange(UnitModel model) {
    return getTranslation().distance(model.getTranslation()) <= getEntity().getCoverage() * 1.2 ? true : false;
  }

  /**
   * Distance to object.
   * 
   * @param model
   *          The model toset
   * @return distance
   */
  public double distance(UnitModel model) {
    return getTranslation().distance(model.getTranslation());
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
