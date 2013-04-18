package models.entity.game;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.validation.constraints.NotNull;

import models.entity.BaseModel;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * @author Sebastian Sachtleben
 */
@Entity
@SuppressWarnings("serial")
public class Environment extends BaseModel {

  @Id
  private Long id;

  @NotNull
  private String name;

  @OneToMany
  @OrderColumn
  @JoinColumn(name = "parent_id")
  private List<Environment> children = new LinkedList<Environment>();

  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_id", insertable = false, updatable = false)
  private Environment parent;

  private static final Finder<Long, Environment> finder = new Finder<Long, Environment>(Long.class, Environment.class);

  public Environment() { }

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

  public List<Environment> getChildren() {
    return children;
  }

  public void setChildren(List<Environment> children) {
    this.children = children;
  }

  public Environment getParent() {
    return parent;
  }

  public void setParent(Environment parent) {
    this.parent = parent;
  }

  public static Finder<Long, Environment> getFinder() {
    return finder;
  }

  @Override
  public String toString() {
    return "Environment [id=" + id + ", name=" + name + "]";
  }

}
