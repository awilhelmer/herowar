package models.entity.game;

import java.beans.Transient;
import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
public class Material implements Serializable {
  private static final long serialVersionUID = 1651915135235L;

  @Id
  private Long id;

  private String name;

  @Lob
  private String map;

  // For mapping while saving
  private Long backBoneId;

  private String color;

  private Boolean transparent;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JsonIgnore
  private Set<MapMaterials> materialsMap;

  @Column(precision = 2)
  private Float opacity;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getMap() {
    return map;
  }

  public void setMap(String map) {
    this.map = map;
  }

  @Transient
  public Long getBackBoneId() {
    return backBoneId;
  }

  public void setBackBoneId(Long backBoneId) {
    this.backBoneId = backBoneId;
  }

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public Boolean getTransparent() {
    return transparent;
  }

  public void setTransparent(Boolean transparent) {
    this.transparent = transparent;
  }

  public Float getOpacity() {
    return opacity;
  }

  public void setOpacity(Float opacity) {
    this.opacity = opacity;
  }

  public Set<MapMaterials> getMaterialsMap() {
    return materialsMap;
  }

  public void setMaterialsMap(Set<MapMaterials> materialsMap) {
    this.materialsMap = materialsMap;
  }

}
