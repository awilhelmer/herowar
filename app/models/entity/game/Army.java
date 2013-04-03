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
public class Army extends BaseModel {

  @Id
  private Long id;

  @ManyToOne
  private Wave wave;

  @OneToMany(cascade = CascadeType.ALL)
  private List<Unit> units;

  private static final Finder<Long, Army> finder = new Finder<Long, Army>(Long.class, Army.class);

  // GETTER & SETTER //

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Wave getWave() {
    return wave;
  }

  public void setWave(Wave wave) {
    this.wave = wave;
  }

  public List<Unit> getUnits() {
    return units;
  }

  public void setUnits(List<Unit> units) {
    this.units = units;
  }

  public static Finder<Long, Army> getFinder() {
    return finder;
  }
}