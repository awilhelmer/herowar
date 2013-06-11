package game.network.server;

import game.network.ObjectPacket;
import game.network.PacketType;
import models.entity.game.Unit;
import models.entity.game.Vector3;

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
  protected int moveSpeed;
  protected double scaleGlow;
  protected boolean burning;
  protected boolean explode;
  protected Vector3 rotation;
  protected long path;

  public ObjectInPacket(long id, Unit unit, long path) {
    super(id, null);
    this.type = PacketType.ObjectInPacket;
    this.name = unit.getName();
    this.utype = unit.getType().ordinal();
    this.health = unit.getHealth();
    this.shield = unit.getShield();
    this.moveSpeed = unit.getMoveSpeed();
    this.scaleGlow = unit.getScaleGlow();
    this.burning = unit.getBurning();
    this.explode = unit.getExplode();
    this.rotation = unit.getRotation();
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
  
  public int getMoveSpeed() {
    return moveSpeed;
  }

  public void setMoveSpeed(int moveSpeed) {
    this.moveSpeed = moveSpeed;
  }

  public double getScaleGlow() {
    return scaleGlow;
  }

  public void setScaleGlow(double scaleGlow) {
    this.scaleGlow = scaleGlow;
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

  public Vector3 getRotation() {
    return rotation;
  }

  public void setRotation(Vector3 rotation) {
    this.rotation = rotation;
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
