package models.entity.game;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * @author Sebastian Sachtleben
 */
@Entity
@SuppressWarnings("serial")
public class Wave implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  private Integer prepareTime;
  private Integer waveTime;
  private Integer quantity;
  private Integer sortOder;
  
  @ManyToOne(cascade = CascadeType.REFRESH)
  @JoinColumn(name = "map_id")
  @JsonIgnore
  private Map map;

  // @OneToMany(cascade = CascadeType.ALL, mappedBy="wave")
  // private Set<Army> armies;

  @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinTable(name = "wave_units")
  @JsonIgnore
  private Set<Unit> units;

  @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.REFRESH })
  @JoinColumn(name = "path_id")
  @JsonIgnore
  private Path path;

  @Transient
  private Long pathId;

  @Transient
  private List<Long> unitIds;

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

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getPrepareTime() {
    return prepareTime;
  }

  public void setPrepareTime(Integer prepareTime) {
    this.prepareTime = prepareTime;
  }

  public Integer getWaveTime() {
    return waveTime;
  }

  public void setWaveTime(Integer waveTime) {
    this.waveTime = waveTime;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public Path getPath() {
    return path;
  }

  public void setPath(Path path) {
    this.path = path;
  }

  public Long getPathId() {
    return pathId;
  }

  public void setPathId(Long pathId) {
    this.pathId = pathId;
  }

  public List<Long> getUnitIds() {
    return unitIds;
  }

  public void setUnitIds(List<Long> unitIds) {
    this.unitIds = unitIds;
  }

  public Integer getSortOder() {
    return sortOder;
  }

  public void setSortOder(Integer sortOder) {
    this.sortOder = sortOder;
  }

  // public Set<Army> getArmies() {
  // return armies;
  // }
  //
  // public void setArmies(Set<Army> armies) {
  // this.armies = armies;
  // }

}
