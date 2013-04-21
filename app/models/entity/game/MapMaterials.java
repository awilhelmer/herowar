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
@Table(name = "map_materials")
public class MapMaterials implements Serializable {
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
    MapMaterials other = (MapMaterials) obj;
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
    @JoinColumn(name = "map_id", referencedColumnName = "id")
    @JsonIgnore
    private Map map;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "mat_id", referencedColumnName = "id")
    @JsonIgnore
    private Material material;

    public Map getMap() {
      return map;
    }

    public void setMap(Map map) {
      this.map = map;
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
      result = prime * result + ((map == null) ? 0 : map.hashCode());
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
      if (map == null) {
        if (other.map != null)
          return false;
      } else if (!map.equals(other.map))
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
