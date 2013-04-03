package models.entity.game;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import models.entity.BaseModel;

/**
 * @author Sebastian Sachtleben
 */
@Entity
@SuppressWarnings("serial")
public class Unit extends BaseModel {

  @Id
  private Long id;

  @ManyToOne
  private Army army;

  private static final Finder<Long, Unit> finder = new Finder<Long, Unit>(Long.class, Unit.class);

  // GETTER & SETTER //

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Army getArmy() {
    return army;
  }

  public void setArmy(Army army) {
    this.army = army;
  }

  public static Finder<Long, Unit> getFinder() {
    return finder;
  }
}
