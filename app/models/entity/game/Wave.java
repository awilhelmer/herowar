package models.entity.game;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import models.entity.BaseModel;

/**
 * @author Sebastian Sachtleben
 */
@Entity
@SuppressWarnings("serial")
public class Wave extends BaseModel {

  @Id
  private Long id;
  
  @ManyToOne
  private Map map;
  
  @OneToMany(cascade = CascadeType.ALL)
  private List<Army> armies;

  private static final Finder<Long, Army> finder = new Finder<Long, Army>(Long.class, Army.class);
  
  // GETTER & SETTER //  
  
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Map getMap() {
    return map;
  }

  public void setMap(Map map) {
    this.map = map;
  }

  public List<Army> getArmies() {
    return armies;
  }

  public void setArmies(List<Army> armies) {
    this.armies = armies;
  }

  public static Finder<Long, Army> getFinder() {
    return finder;
  }
}
