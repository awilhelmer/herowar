package models.entity.game;

import javax.persistence.Entity;
import javax.persistence.Id;

import models.entity.BaseModel;

/**
 * @author Sebastian Sachtleben
 */
@Entity
@SuppressWarnings("serial")
public class Map extends BaseModel {

  @Id
  private Long id;
  
  private String name;
  private String description;
  private Integer teamSize;
  
  private static final Finder<Long, Map> finder = new Finder<Long, Map>(Long.class, Map.class);

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

  public Integer getTeamSize() {
    return teamSize;
  }

  public void setTeamSize(Integer teamSize) {
    this.teamSize = teamSize;
  }

  public static Finder<Long, Map> getFinder() {
    return finder;
  }

}
