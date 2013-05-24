package game.network.server;

import game.network.BasePacket;
import game.network.PacketType;

/**
 * Server send GameVictoryPacket on victory.
 * 
 * @author Sebastian Sachtleben
 */
@SuppressWarnings("serial")
public class GameVictoryPacket extends BasePacket {

  public GameVictoryPacket() {
    this.type = PacketType.GameVictoryPacket;
  }

  @Override
  public String toString() {
    return "GameVictoryPacket [type=" + type + "]";
  }
}
