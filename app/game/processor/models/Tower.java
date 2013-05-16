package game.processor.models;

import game.GameSession;
import models.entity.game.Vector3;

/**
 * The Tower is placed somewhere on the map by any player.
 * 
 * @author Sebastian Sachtleben
 */
public class Tower {

  private Long objectId;
  private Long towerId;
  private Vector3 postion;
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

  public GameSession getSession() {
    return session;
  }

  public void setSession(GameSession session) {
    this.session = session;
  }
}
