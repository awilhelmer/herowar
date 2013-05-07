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
public class BasePacket implements Serializable {
  
  protected Integer type;
  
  protected Long createdTime;

  public BasePacket() {
    this.createdTime = (new Date()).getTime();
  }
  
  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
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
    return true;
  }

  @Override
  public String toString() {
    return "BasePacket [type=" + type + "]";
  }
}
