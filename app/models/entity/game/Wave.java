package models.entity.game;

import javax.persistence.Entity;
import javax.persistence.Id;

import models.entity.BaseModel;

/**
 * @author Sebastian Sachtleben
 */
@Entity
@SuppressWarnings("serial")
public class Wave extends BaseModel {

  @Id
  private Long id;

  // GETTER & SETTER //  
  
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
}
