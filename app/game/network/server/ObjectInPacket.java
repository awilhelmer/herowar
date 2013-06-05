package game.network.server;

import game.network.ObjectPacket;
import game.network.PacketType;
import models.entity.game.Unit;

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
  protected boolean burning;
  protected boolean explode;
  protected long path;

  public ObjectInPacket(long id, Unit unit, long path) {
    super(id, null);
    this.type = PacketType.ObjectInPacket;
    this.name = unit.getName();
    this.utype = unit.getType().ordinal();
    this.health = unit.getHealth();
    this.shield = unit.getShield();
    this.burning = unit.getBurning();
    this.explode = unit.getExplode();
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

  public boolean isBurning() {
    return burning;
  }

  public void setBurning(boolean burning) {
    this.burning = burning;
  }

  public boolean isExplode() {
    return explode;
  }

  public void setExplode(boolean explode) {
    this.explode = explode;
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
