package game.network.server;

import java.util.List;

import models.entity.game.Vector3;
import game.network.BasePacket;
import game.network.PacketType;

/**
 * The WaveUpdatePacket will be send from server to client and contains the
 * current wave id and eta of the next wave.
 * 
 * @author Sebastian Sachtleben
 */
@SuppressWarnings("serial")
public class WaveUpdatePacket extends BasePacket {

  protected int current;
  protected long eta;
  protected List<Vector3> positions;

  public WaveUpdatePacket(int current, long eta, List<Vector3> positions) {
    super();
    this.type = PacketType.WaveUpdatePacket;
    this.current = current;
    this.eta = eta;
    this.positions = positions;
  }

  public int getCurrent() {
    return current;
  }

  public void setCurrent(int current) {
    this.current = current;
  }

  public long getEta() {
    return eta;
  }

  public void setEta(long eta) {
    this.eta = eta;
  }
  
  public List<Vector3> getPositions() {
    return positions;
  }

  public void setPositions(List<Vector3> positions) {
    this.positions = positions;
  }

  @Override
  public String toString() {
    return "WaveUpdatePacket [type=" + type + ", createdTime=" + createdTime + ", current=" + current + ", eta=" + eta + "]";
  }
}
