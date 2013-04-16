package models.entity.game;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

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
  private Float smoothness;
  private Integer zScale;

  @OneToOne(mappedBy = "terrain")
  private Map map;

  private static final Finder<Long, Terrain> finder = new Finder<Long, Terrain>(Long.class, Terrain.class);

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

  public static Finder<Long, Terrain> getFinder() {
    return finder;
  }
}