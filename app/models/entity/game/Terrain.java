package models.entity.game;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.codehaus.jackson.annotate.JsonIgnore;

import models.entity.BaseModel;

/**
 * @author Sebastian Sachtleben
 */
@Entity
@SuppressWarnings("serial")
public class Terrain extends BaseModel {

  @Id
  private Long id;

  private Integer width;
  private Integer height;
  @Column(precision = 1)
  private Float smoothness;
  private Integer zScale;
  private Boolean wireframe;

  @JsonIgnore
  @OneToOne(mappedBy = "terrain")
  private Map map;

  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
  private Geometry geometry;

  private static final Finder<Long, Terrain> finder = new Finder<Long, Terrain>(Long.class, Terrain.class);

  public Terrain() {
    this.width = 500;
    this.height = 500;
    this.smoothness = 0.1f;
    this.zScale = 0;
    this.wireframe = true;
  }

  // GETTER & SETTER //

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Integer getWidth() {
    return width;
  }

  public void setWidth(Integer width) {
    this.width = width;
  }

  public Integer getHeight() {
    return height;
  }

  public void setHeight(Integer height) {
    this.height = height;
  }

  public Float getSmoothness() {
    return smoothness;
  }

  public void setSmoothness(Float smoothness) {
    this.smoothness = smoothness;
  }

  public Integer getzScale() {
    return zScale;
  }

  public void setzScale(Integer zScale) {
    this.zScale = zScale;
  }

  public Map getMap() {
    return map;
  }

  public void setMap(Map map) {
    this.map = map;
  }

  public Geometry getGeometry() {
    return geometry;
  }

  public void setGeometry(Geometry geometry) {
    this.geometry = geometry;
  }

  public Boolean getWireframe() {
    return wireframe;
  }

  public void setWireframe(Boolean wireframe) {
    this.wireframe = wireframe;
  }

  public static Finder<Long, Terrain> getFinder() {
    return finder;
  }

  @Override
  public String toString() {
    return "Terrain [id=" + id + ", width=" + width + ", height=" + height + ", smoothness=" + smoothness + ", zScale=" + zScale + "]";
  }
}