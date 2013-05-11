package game.event;

import models.entity.game.Unit;

import models.entity.game.Path;

public class GameUnitEvent {

  public Path path;
  public Unit unit;

  public GameUnitEvent(Path path, Unit unit) {
    super();
    this.path = path;
    this.unit = unit;
  }

  public Path getPath() {
    return path;
  }

  public void setPath(Path path) {
    this.path = path;
  }

  public Unit getUnit() {
    return unit;
  }

  public void setUnit(Unit unit) {
    this.unit = unit;
  }

}
