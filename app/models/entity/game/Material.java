package models.entity.game;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;

import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
public class Material implements Serializable {
  private static final long serialVersionUID = 1651915135235L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  @Lob
  private String map;


  private String color;

  private Boolean transparent;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy="id.material")
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
