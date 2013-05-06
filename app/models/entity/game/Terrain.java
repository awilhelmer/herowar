package models.entity.game;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * @author Sebastian Sachtleben
 */
@Entity
@SuppressWarnings("serial")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Terrain implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Integer width;
  private Integer height;
  @Column(scale = 1)
  private Float smoothness;
  private Integer zScale;
  private Boolean wireframe;

  @JsonIgnore
  @OneToOne(mappedBy = "terrain", cascade = CascadeType.REFRESH)
  private Map map;

  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
  private Geometry geometry;

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

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((height == null) ? 0 : height.hashCode());
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((smoothness == null) ? 0 : smoothness.hashCode());
    result = prime * result + ((width == null) ? 0 : width.hashCode());
    result = prime * result + ((wireframe == null) ? 0 : wireframe.hashCode());
    result = prime * result + ((zScale == null) ? 0 : zScale.hashCode());
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
    Terrain other = (Terrain) obj;
    if (height == null) {
      if (other.height != null)
        return false;
    } else if (!height.equals(other.height))
      return false;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    if (smoothness == null) {
      if (other.smoothness != null)
        return false;
    } else if (!smoothness.equals(other.smoothness))
      return false;
    if (width == null) {
      if (other.width != null)
        return false;
    } else if (!width.equals(other.width))
      return false;
    if (wireframe == null) {
      if (other.wireframe != null)
        return false;
    } else if (!wireframe.equals(other.wireframe))
      return false;
    if (zScale == null) {
      if (other.zScale != null)
        return false;
    } else if (!zScale.equals(other.zScale))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "Terrain [id=" + id + ", width=" + width + ", height=" + height + ", smoothness=" + smoothness + ", zScale=" + zScale + "]";
  }
}