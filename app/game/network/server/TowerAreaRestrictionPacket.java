package game.network.server;

import game.models.TowerRestriction;
import game.network.BasePacket;
import game.network.PacketType;
import models.entity.game.Vector3;

/**
 * The TowerAreaRescritionPacket will be send from server to client and tell
 * where tower can be build.
 * 
 * @author Sebastian Sachtleben
 */
@SuppressWarnings("serial")
public class TowerAreaRestrictionPacket extends BasePacket {

  protected Vector3 position;
  protected int radius;

  public TowerAreaRestrictionPacket(TowerRestriction restriction) {
    super();
    this.type = PacketType.TowerAreaRestrictionPacket;
    this.position = restriction.getPosition();
    this.radius = restriction.getRadius();
  }

  public int getRadius() {
    return radius;
  }

  public void setRadius(int radius) {
    this.radius = radius;
  }

  public Vector3 getPosition() {
    return position;
  }

  public void setPosition(Vector3 position) {
    this.position = position;
  }

  @Override
  public String toString() {
    return "TowerAreaRestrictionPacket [type=" + type + ", createdTime=" + createdTime + "]";
  }
}
