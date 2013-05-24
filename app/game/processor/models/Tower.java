package game.processor.models;

import game.GameSession;

import java.io.Serializable;

import models.entity.game.Unit;
import models.entity.game.Vector3;

/**
 * The Tower is placed somewhere on the map by any player.
 * 
 * @author Sebastian Sachtleben
 */
@SuppressWarnings("serial")
public class Tower implements Serializable {

  private Long objectId;
  private Long towerId;
  private Vector3 postion;
  private Unit target;
  private GameSession session;

  public Long getObjectId() {
    return objectId;
  }

  public void setObjectId(Long objectId) {
    this.objectId = objectId;
  }

  public Long getTowerId() {
    return towerId;
  }

  public void setTowerId(Long towerId) {
    this.towerId = towerId;
  }

  public Vector3 getPostion() {
    return postion;
  }

  public void setPostion(Vector3 postion) {
    this.postion = postion;
  }

  public Unit getTarget() {
    return target;
  }

  public void setTarget(Unit target) {
    this.target = target;
  }

  public GameSession getSession() {
    return session;
  }

  public void setSession(GameSession session) {
    this.session = session;
  }
}
