package game.network.server;

import models.entity.game.Vector3;
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

  protected String name;
  protected long path;
  
  public ObjectInPacket(long id, String name, long path) {
    super(id, null);
    this.type = PacketType.ObjectInPacket;
    this.name = name;
    this.path = path;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
  
  public long getPath() {
    return path;
  }

  public void setPath(long path) {
    this.path = path;
  }

  @Override
  public String toString() {
    return "ObjectInPacket [type=" + type + ", id=" + id + ", name=" + name + ", path=" + path + ", position=" + position.toString() + "]";
  }
}
