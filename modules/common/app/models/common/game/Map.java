package models.common.game;

import javax.persistence.Entity;

import play.db.ebean.Model;

/**
 * @author Sebastian Sachtleben
 */
@Entity
@SuppressWarnings("serial")
public class Map extends Model {

  private String name;
  private Integer teamSize;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getTeamSize() {
    return teamSize;
  }

  public void setTeamSize(Integer teamSize) {
    this.teamSize = teamSize;
  }

}
