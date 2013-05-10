package game.network.server;

import game.network.PacketType;

/**
 * The WaveInitPacket will be send from server to client with initial waves
 * informations. It contains the current wave information from WaveUpdatePacket
 * and additional the total amount of waves.
 * 
 * @author Sebastian Sachtleben
 */
@SuppressWarnings("serial")
public class WaveInitPacket extends WaveUpdatePacket {

  private int total;

  public WaveInitPacket(int current, long eta, int total) {
    super(current, eta);
    this.type = PacketType.WaveInitPacket;
    this.total = total;
  }

  public int getTotal() {
    return total;
  }

  public void setTotal(int total) {
    this.total = total;
  }

  @Override
  public String toString() {
    return "WaveInitPacket [type=" + type + ", createdTime=" + createdTime + ", total=" + total + ", current=" + current + ", eta=" + eta + "]";
  }
}
