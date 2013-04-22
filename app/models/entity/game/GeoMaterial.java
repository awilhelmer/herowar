package models.entity.game;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
@Table(name = "geo_material")
public class GeoMaterial implements Serializable {
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private PK id;

  @NotNull
  private Long arrayIndex;

  public PK getId() {
    return id;
  }

  public void setId(PK id) {
    this.id = id;
  }

  public Long getArrayIndex() {
    return arrayIndex;
  }

  public void setArrayIndex(Long arrayIndex) {
    this.arrayIndex = arrayIndex;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((arrayIndex == null) ? 0 : arrayIndex.hashCode());
    result = prime * result + ((id == null) ? 0 : id.hashCode());
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
    GeoMaterial other = (GeoMaterial) obj;
    if (arrayIndex == null) {
      if (other.arrayIndex != null)
        return false;
    } else if (!arrayIndex.equals(other.arrayIndex))
      return false;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    return true;
  }

  @Embeddable
  public static class PK implements Serializable {
    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "geo_id", referencedColumnName = "id")
    @JsonIgnore
    private Geometry geometry;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "mat_id", referencedColumnName = "id")
    @JsonIgnore
    private Material material;

    public Geometry getGeometry() {
      return geometry;
    }

    public void setGeometry(Geometry geometry) {
      this.geometry = geometry;
    }

    public Material getMaterial() {
      return material;
    }

    public void setMaterial(Material material) {
      this.material = material;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((geometry == null) ? 0 : geometry.hashCode());
      result = prime * result + ((material == null) ? 0 : material.hashCode());
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
      PK other = (PK) obj;
      if (geometry == null) {
        if (other.geometry != null)
          return false;
      } else if (!geometry.equals(other.geometry))
        return false;
      if (material == null) {
        if (other.material != null)
          return false;
      } else if (!material.equals(other.material))
        return false;
      return true;
    }

  }

}
