package game.network.server;

import game.network.BasePacket;
import game.network.PacketType;

/**
 * Server sends informations about the current and all waves.
 * 
 * @author Sebastian Sachtleben
 */
@SuppressWarnings("serial")
public class WaveUpdatePacket extends BasePacket {

  public WaveUpdatePacket() {
    super();
    this.type = PacketType.WaveUpdatePacket;
  }

  @Override
  public String toString() {
    return "WaveUpdatePacket [type=" + type + ", createdTime=" + createdTime + "]";
  }
}
