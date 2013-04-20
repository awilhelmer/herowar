package models.entity.game;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * @author Sebastian Sachtleben
 */
@Entity
@SuppressWarnings("serial")
public class Environment implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  private String name;

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "parent_id")
  private Set<Environment> children = new HashSet<Environment>();

  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
  @JoinColumn(name = "parent_id")
  private Environment parent;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private Geometry geometry;

  public Environment() {
  }

  public Environment(String name) {
    this.name = name;
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

  public Set<Environment> getChildren() {
    return children;
  }

  public void setChildren(Set<Environment> children) {
    this.children = children;
  }

  public Environment getParent() {
    return parent;
  }

  public void setParent(Environment parent) {
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
    return "Environment [id=" + id + ", name=" + name + "]";
  }

}
