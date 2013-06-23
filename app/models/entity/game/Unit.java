package models.entity.game;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * @author Sebastian Sachtleben
 */
@Entity
@Table(name = "unit", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
@SuppressWarnings("serial")
public class Unit implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  private String name;

  private Integer health = 0;
  private Integer shield = 0;
  private Integer moveSpeed = 0;
  private Integer rewardScore = 0;
  private Integer rewardGold = 0;
  private Integer reduceLives = 1;
  private Double scaleGlow = 1d;
  private UnitType type;
  private Boolean burning = false;
  private Boolean explode = false;

  @Embedded
  @AttributeOverrides({ @AttributeOverride(name = "x", column = @Column(name = "rotation_x")),
      @AttributeOverride(name = "y", column = @Column(name = "rotation_y")), @AttributeOverride(name = "z", column = @Column(name = "rotation_z")) })
  private Vector3 rotation = new Vector3();

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "parent")
  private Set<Unit> children = new HashSet<Unit>();

  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
  @JoinColumn(name = "parent_id")
  private Unit parent;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private Geometry geometry;

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

  public Integer getHealth() {
    return health;
  }

  public void setHealth(Integer health) {
    this.health = health;
  }

  public Integer getShield() {
    return shield;
  }

  public void setShield(Integer shield) {
    this.shield = shield;
  }

  public Integer getMoveSpeed() {
    return moveSpeed;
  }

  public void setMoveSpeed(Integer moveSpeed) {
    this.moveSpeed = moveSpeed;
  }
  
  public Integer getRewardScore() {
    return rewardScore;
  }

  public void setRewardScore(Integer rewardScore) {
    this.rewardScore = rewardScore;
  }

  public Integer getRewardGold() {
    return rewardGold;
  }

  public void setRewardGold(Integer rewardGold) {
    this.rewardGold = rewardGold;
  }

  public Integer getReduceLives() {
    return reduceLives;
  }

  public void setReduceLives(Integer reduceLives) {
    this.reduceLives = reduceLives;
  }

  public Double getScaleGlow() {
    return scaleGlow;
  }

  public void setScaleGlow(Double scaleGlow) {
    this.scaleGlow = scaleGlow;
  }

  public UnitType getType() {
    return type;
  }

  public void setType(UnitType type) {
    this.type = type;
  }

  public Boolean getBurning() {
    return burning;
  }

  public void setBurning(Boolean burning) {
    this.burning = burning;
  }

  public Boolean getExplode() {
    return explode;
  }

  public void setExplode(Boolean explode) {
    this.explode = explode;
  }
  
  public Vector3 getRotation() {
    return rotation;
  }

  public void setRotation(Vector3 rotation) {
    this.rotation = rotation;
  }

  public Set<Unit> getChildren() {
    return children;
  }

  public void setChildren(Set<Unit> children) {
    this.children = children;
  }

  public Unit getParent() {
    return parent;
  }

  public void setParent(Unit parent) {
    this.parent = parent;
  }

  public Geometry getGeometry() {
    return geometry;
  }

  public void setGeometry(Geometry geometry) {
    this.geometry = geometry;
  }

  @Override
  public String toString() {
    return "Unit [id=" + id + ", name=" + name + "]";
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((burning == null) ? 0 : burning.hashCode());
    result = prime * result + ((explode == null) ? 0 : explode.hashCode());
    result = prime * result + ((health == null) ? 0 : health.hashCode());
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    result = prime * result + ((shield == null) ? 0 : shield.hashCode());
    result = prime * result + ((type == null) ? 0 : type.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Unit other = (Unit) obj;
    if (burning == null) {
      if (other.burning != null)
        return false;
    } else if (!burning.equals(other.burning))
      return false;
    if (explode == null) {
      if (other.explode != null)
        return false;
    } else if (!explode.equals(other.explode))
      return false;
    if (health == null) {
      if (other.health != null)
        return false;
    } else if (!health.equals(other.health))
      return false;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    if (name == null) {
      if (other.name != null)
        return false;
    } else if (!name.equals(other.name))
      return false;
    if (shield == null) {
      if (other.shield != null)
        return false;
    } else if (!shield.equals(other.shield))
      return false;
    if (type != other.type)
      return false;
    return true;
  }
}
