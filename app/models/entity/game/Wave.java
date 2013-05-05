package models.entity.game;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

/**
 * @author Sebastian Sachtleben
 */
@Entity
@SuppressWarnings("serial")
public class Wave implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  private Map map;

  // @OneToMany(cascade = CascadeType.ALL, mappedBy="wave")
  // private Set<Army> armies;

  @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinTable(name = "wave_units")
  private Set<Unit> units;

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

  public Set<Unit> getUnits() {
    return units;
  }

  public void setUnits(Set<Unit> units) {
    this.units = units;
  }

  // public Set<Army> getArmies() {
  // return armies;
  // }
  //
  // public void setArmies(Set<Army> armies) {
  // this.armies = armies;
  // }

}
