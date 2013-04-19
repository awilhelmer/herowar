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
import javax.persistence.ManyToMany;

import play.db.ebean.Model.Finder;

@Entity
public class Material implements Serializable {
  private static final long serialVersionUID = 1651915135235L;

  private static final Finder<Long, Material> finder = new Finder<Long, Material>(Long.class, Material.class);
  @Id
  private Long id;

  private String name;

  @Lob
  private String map;

  // For mapping while saving
  private Integer backBoneId;

  private String color;

  private Boolean transparent;

  @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "materials")
  private Set<Map> maps;

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
  public Integer getBackBoneId() {
    return backBoneId;
  }

  public void setBackBoneId(Integer backBoneId) {
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

  public Set<Map> getMaps() {
    return maps;
  }

  public void setMaps(Set<Map> maps) {
    this.maps = maps;
  }

  public static Finder<Long, Material> getFinder() {
    return finder;
  }

}
