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

@Entity
@Table(name="map_materials")
public class MapMaterials implements Serializable {
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private PK id;

  @NotNull
  private Long backBoneId;

  public PK getId() {
    return id;
  }

  public void setId(PK id) {
    this.id = id;
  }

  public Long getBackBoneId() {
    return backBoneId;
  }

  public void setBackBoneId(Long backBoneId) {
    this.backBoneId = backBoneId;
  }

  @Embeddable
  public static class PK implements Serializable {
    private static final long serialVersionUID = 1L;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "map_id", referencedColumnName = "id")
    private Map map;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "mat_id", referencedColumnName = "id")
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

  }

}
