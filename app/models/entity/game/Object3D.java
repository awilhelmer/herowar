package models.entity.game;

import javax.persistence.Entity;
import javax.persistence.Id;

import models.entity.BaseModel;

/**
 * @author Sebastian Sachtleben
 */
@Entity
@SuppressWarnings("serial")
public class Object3D extends BaseModel {

  @Id
  private Long id;
  
  private String name;
  private String description;
  
  private static final Finder<Long, Object3D> finder = new Finder<Long, Object3D>(Long.class, Object3D.class);
  
  public static void merge(Object3D object, Object3D object2) {
    object.setName(object2.getName());
    object.setDescription(object2.getDescription());
  } 
  
  // GETTER & SETTER //
  
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

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public static Finder<Long, Object3D> getFinder() {
    return finder;
  } 
}
