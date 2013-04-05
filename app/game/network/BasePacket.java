package game.network;

import java.io.Serializable;
import java.util.Date;

/**
 * Used to detect packet type.
 * 
 * @author Alexander Wilhelmer
 */
public class BasePacket implements Serializable {
  private static final long serialVersionUID = 7544376025771190487L;
  
  protected Integer type;
  protected Long objectId;
  protected Long createdTime;

  public BasePacket() {
    this.createdTime = new Date().getTime();
  }

  public BasePacket(Long objectId) {
    super();
    this.objectId = objectId;
    this.createdTime = new Date().getTime();
  }

  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  public Long getObjectId() {
    return objectId;
  }

  public void setObjectId(Long objectId) {
    this.objectId = objectId;
  }
  
  public Long getCreatedTime() {
    return createdTime;
  }

  public void setCreatedTime(Long createdTime) {
    this.createdTime = createdTime;
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
    BasePacket other = (BasePacket) obj;
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
