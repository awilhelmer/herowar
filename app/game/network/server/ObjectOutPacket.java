package game.network.server;

import game.network.ObjectPacket;
import game.network.PacketType;

/**
 * The ObjectOutPacket will be send from server to client to tell a object left
 * the map.
 * 
 * @author Sebastian Sachtleben
 */
@SuppressWarnings("serial")
public class ObjectOutPacket extends ObjectPacket {

  public ObjectOutPacket(long id) {
    super(id, null);
    this.type = PacketType.ObjectOutPacket;
  }

  @Override
  public String toString() {
    return "ObjectOutPacket [type=" + type + ", id=" + id + ", position=" + position.toString() + "]";
  }
}
