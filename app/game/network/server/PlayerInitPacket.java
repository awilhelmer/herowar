package game.network.server;

import game.GameSession;
import game.network.ObjectPacket;
import game.network.PacketType;

/**
 * Server sends info about current player.
 * 
 * @author Sebastian Sachtleben
 */
@SuppressWarnings("serial")
public class PlayerInitPacket extends ObjectPacket {

  private GameSession object;

  public PlayerInitPacket(Long objectId, GameSession object) {
    super(objectId);
    this.object = object;

    this.type = PacketType.PlayerInitPacket;
  }

  public GameSession getObject() {
    return object;
  }

  public void setObject(GameSession object) {
    this.object = object;
  }

}
