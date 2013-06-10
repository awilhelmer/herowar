package models.entity.game;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * @author Sebastian Sachtleben
 */
@Entity
@SuppressWarnings("serial")
public class TowerWeapon implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JsonIgnore
  private Tower tower;

  private TowerWeaponType type;
  private Vector3 position;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Tower getTower() {
    return tower;
  }

  public void setTower(Tower tower) {
    this.tower = tower;
  }

  public TowerWeaponType getType() {
    return type;
  }

  public void setType(TowerWeaponType type) {
    this.type = type;
  }

  public Vector3 getPosition() {
    return position;
  }

  public void setPosition(Vector3 position) {
    this.position = position;
  }

  @Override
  public String toString() {
    return "TowerWeapon [id=" + id + ", type=" + type + ", position=" + (position != null ? position.toString() : null) + "]";
  }
}
