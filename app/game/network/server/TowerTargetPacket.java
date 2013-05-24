package game.network.server;

import game.network.BasePacket;
import game.network.PacketType;

/**
 * The TowerTargetPacket will be send from server to client once a tower changed
 * his target.
 * 
 * @author Sebastian Sachtleben
 */
@SuppressWarnings("serial")
public class TowerTargetPacket extends BasePacket {

  protected long tower;
  protected long target;

  public TowerTargetPacket(long tower, long target) {
    super();
    this.type = PacketType.TowerTargetPacket;
    this.tower = tower;
    this.target = target;
  }

  public long getTower() {
    return tower;
  }

  public void setTower(long tower) {
    this.tower = tower;
  }

  public long getTarget() {
    return target;
  }

  public void setTarget(long target) {
    this.target = target;
  }
}
