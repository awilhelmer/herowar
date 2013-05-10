package game.network.server;

import game.network.ObjectPacket;
import game.network.PacketType;

/**
 * The ObjectInPacket will be send from server to client to tell a new object
 * entered the map.
 * 
 * @author Sebastian Sachtleben
 */
@SuppressWarnings("serial")
public class ObjectInPacket extends ObjectPacket {

  public ObjectInPacket() {
    this.type = PacketType.ObjectInPacket;
  }

  @Override
  public String toString() {
    return "ObjectInPacket [type=" + type + "]";
  }
}
