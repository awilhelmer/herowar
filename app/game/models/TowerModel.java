package game.models;

import game.GameSession;

import java.util.Set;

/**
 * The Tower is placed somewhere on the map by any player.
 * 
 * @author Sebastian Sachtleben
 */
@SuppressWarnings("serial")
public class TowerModel extends BaseModel {
  public static final int RANGE = 100;

  private UnitModel target;
  private GameSession session;

  public TowerModel(Long id, Long dbId) {
    super(id, dbId);
  }

  /**
   * Rotate to current waypoint.
   * 
   * @param delta
   *          The delta to set
   */
  public void rotateTo(Double delta) {
    super.rotateTo(getTarget().getTranslation().clone(), delta);
  }

  /**
   * Find a valid target for this tower.
   * 
   * @param units
   *          The units to set
   * @return UnitModel The best target.
   */
  public UnitModel findTarget(Set<UnitModel> units) {
    if (getTarget() != null && inRange(getTarget())) {
      return getTarget();
    }
    for (UnitModel unit : units) {
      if (inRange(unit)) {
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
}
