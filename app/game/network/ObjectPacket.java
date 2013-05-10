package game.network;

import models.entity.game.Vector3;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Abstract ObjectPacket class for several object packets.
 * 
 * @author Sebastian Sachtleben
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressWarnings("serial")
public abstract class ObjectPacket extends BasePacket {
  
  protected long id;
  protected Vector3 position;

  public ObjectPacket(long id, Vector3 position) {
    super();
    this.id = id;
    this.position = position;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public Vector3 getPosition() {
    return position;
  }

  public void setPosition(Vector3 position) {
    this.position = position;
  }
}
