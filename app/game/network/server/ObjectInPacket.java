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

  protected String name;
  protected int utype;
  protected int health;
  protected int shield;
  protected long path;

  public ObjectInPacket(long id, String name, int utype, int health, int shield, long path) {
    super(id, null);
    this.type = PacketType.ObjectInPacket;
    this.name = name;
    this.utype = utype;
    this.health = health;
    this.shield = shield;
    this.path = path;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getUtype() {
    return utype;
  }

  public void setUtype(int utype) {
    this.utype = utype;
  }

  public int getHealth() {
    return health;
  }

  public void setHealth(int health) {
    this.health = health;
  }

  public int getShield() {
    return shield;
  }

  public void setShield(int shield) {
    this.shield = shield;
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
