package models.entity.game;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.Transient;

import com.ardor3d.math.type.ReadOnlyVector3;

@Embeddable
public class Vector3 implements Serializable {
  private static final long serialVersionUID = 6203694060229875926L;

  private Double x;
  private Double y;
  private Double z;

  @Transient
  private com.ardor3d.math.Vector3 ardorVector;

  public Vector3() {
    this.x = 0d;
    this.y = 0d;
    this.z = 0d;
  }

  public Vector3(Double x, Double y, Double z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public Double getX() {
    return x;
  }

  public void setX(Double x) {
    this.x = x;
  }

  public Double getY() {
    return y;
  }

  public void setY(Double y) {
    this.y = y;
  }

  public Double getZ() {
    return z;
  }

  public void setZ(Double z) {
    this.z = z;
  }

  public com.ardor3d.math.Vector3 getArdorVector() {
    if (ardorVector == null) {
      ardorVector = new com.ardor3d.math.Vector3(x, y, z);
    }
    return ardorVector;
  }

  public void setArdorVector(com.ardor3d.math.Vector3 ardorVector) {
    this.ardorVector = ardorVector;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((x == null) ? 0 : x.hashCode());
    result = prime * result + ((y == null) ? 0 : y.hashCode());
    result = prime * result + ((z == null) ? 0 : z.hashCode());
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
    Vector3 other = (Vector3) obj;
    if (x == null) {
      if (other.x != null)
        return false;
    } else if (!x.equals(other.x))
      return false;
    if (y == null) {
      if (other.y != null)
        return false;
    } else if (!y.equals(other.y))
      return false;
    if (z == null) {
      if (other.z != null)
        return false;
    } else if (!z.equals(other.z))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "Vector3 [x=" + x + ", y=" + y + ", z=" + z + "]";
  }

}
