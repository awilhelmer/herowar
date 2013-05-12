package game.models;

import models.entity.game.Path;
import models.entity.game.Waypoint;

public class UnitModel extends BaseModel {
  private static final long serialVersionUID = 1L;

  private Path activePath;

  private Waypoint activeWaypoint;

  private boolean endPointReached = false;

  public UnitModel(Long id, Long dbId) {
    super(id, dbId);
  }

  public Path getActivePath() {
    return activePath;
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
