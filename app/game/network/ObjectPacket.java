package game.network;

import java.io.Serializable;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Used to detect packet type.
 * 
 * @author Alexander Wilhelmer
 * @author Sebastian Sachtleben
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressWarnings("serial")
public class ObjectPacket extends BasePacket {
  
  protected Long objectId;

  public ObjectPacket() {
    this.createdTime = new Date().getTime();
  }

  public ObjectPacket(Long objectId) {
    super();
    this.objectId = objectId;
    this.createdTime = new Date().getTime();
  }

  public Long getObjectId() {
    return objectId;
  }

  public void setObjectId(Long objectId) {
    this.objectId = objectId;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((type == null) ? 0 : type.hashCode());
    result = prime * result + ((objectId == null) ? 0 : objectId.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ObjectPacket other = (ObjectPacket) obj;
    if (type == null) {
      if (other.type != null)
        return false;
    } else if (!type.equals(other.type))
      return false;
    if (objectId == null) {
      if (other.objectId != null)
        return false;
    } else if (!objectId.equals(other.objectId))
      return false;
    return true;
  }

}
