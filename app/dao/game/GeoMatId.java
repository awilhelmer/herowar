package dao.game;

import java.io.Serializable;

/**
 * For mapping Material in Geometry
 * @author TimeX
 *
 */
public class GeoMatId implements Serializable {
  private static final long serialVersionUID = 1L;
  private Long materialIndex;
  private Long materialId;

  public Long getMaterialIndex() {
    return materialIndex;
  }

  public void setMaterialIndex(Long materialIndex) {
    this.materialIndex = materialIndex;
  }

  public Long getMaterialId() {
    return materialId;
  }

  public void setMaterialId(Long materialId) {
    this.materialId = materialId;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((materialId == null) ? 0 : materialId.hashCode());
    result = prime * result + ((materialIndex == null) ? 0 : materialIndex.hashCode());
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
    GeoMatId other = (GeoMatId) obj;
    if (materialId == null) {
      if (other.materialId != null)
        return false;
    } else if (!materialId.equals(other.materialId))
      return false;
    if (materialIndex == null) {
      if (other.materialIndex != null)
        return false;
    } else if (!materialIndex.equals(other.materialIndex))
      return false;
    return true;
  }

}
