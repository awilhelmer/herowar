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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import models.entity.BaseModel;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * @author Sebastian Sachtleben
 */
@Entity
@SuppressWarnings("serial")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Map extends BaseModel implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
  private String description;
  private String skybox;
  private Integer teamSize;
  private Integer prepareTime;
  private Integer lives;
  private Integer goldStart;
  private Integer goldPerTick;

  @OneToOne(cascade = CascadeType.ALL)
  private Terrain terrain;

  @OneToMany(cascade = CascadeType.ALL)
  private Set<Wave> waves;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "map")
  private Set<Mesh> objects;

  @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinTable(name = "map_materials")
  @JsonIgnore
  private Set<Material> allMaterials;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "map")
  private Set<Path> paths;

  @Transient
  private List<Material> materials;

  @Transient
  private List<Geometry> staticGeometries;

  public Map() {
    this.name = "";
    this.description = "";
    this.skybox = "default";
    this.teamSize = 1;
    this.prepareTime = 500;
    this.lives = 20;
    this.goldStart = 2000;
    this.goldPerTick = 5;
    this.terrain = new Terrain();
    this.getTerrain().setGeometry(new Geometry());
    this.getTerrain().getGeometry().setTerrain(this.getTerrain());
    this.getTerrain().getGeometry().setMetadata(new GeoMetaData());
    this.getTerrain().getGeometry().getMetadata().setGeometry(this.getTerrain().getGeometry());
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

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getSkybox() {
    return skybox;
  }

  public void setSkybox(String skybox) {
    this.skybox = skybox;
  }

  public Integer getTeamSize() {
    return teamSize;
  }

  public void setTeamSize(Integer teamSize) {
    this.teamSize = teamSize;
  }

  public Integer getPrepareTime() {
    return prepareTime;
  }

  public void setPrepareTime(Integer prepareTime) {
    this.prepareTime = prepareTime;
  }

  public Integer getLives() {
    return lives;
  }

  public void setLives(Integer lives) {
    this.lives = lives;
  }

  public Integer getGoldStart() {
    return goldStart;
  }

  public void setGoldStart(Integer goldStart) {
    this.goldStart = goldStart;
  }

  public Integer getGoldPerTick() {
    return goldPerTick;
  }

  public void setGoldPerTick(Integer goldPerTick) {
    this.goldPerTick = goldPerTick;
  }

  public Terrain getTerrain() {
    return terrain;
  }

  public void setTerrain(Terrain terrain) {
    this.terrain = terrain;
  }

  public Set<Wave> getWaves() {
    return waves;
  }

  public void setWaves(Set<Wave> waves) {
    this.waves = waves;
  }

  public Set<Material> getAllMaterials() {
    return allMaterials;
  }

  public void setAllMaterials(Set<Material> allMaterials) {
    this.allMaterials = allMaterials;
  }

  public Set<Mesh> getObjects() {
    return objects;
  }

  public void setObjects(Set<Mesh> objects) {
    this.objects = objects;
  }

  public List<Material> getMaterials() {
    return materials;
  }

  public void setMaterials(List<Material> materials) {
    this.materials = materials;
  }

  public List<Geometry> getStaticGeometries() {
    return staticGeometries;
  }

  public void setStaticGeometries(List<Geometry> staticGeometries) {
    this.staticGeometries = staticGeometries;
  }

  public Set<Path> getPaths() {
    return paths;
  }

  public void setPaths(Set<Path> paths) {
    this.paths = paths;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((description == null) ? 0 : description.hashCode());
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((name == null) ? 0 : name.hashCode());
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
    Map other = (Map) obj;
    if (description == null) {
      if (other.description != null)
        return false;
    } else if (!description.equals(other.description))
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
    return true;
  }

  @Override
  public String toString() {
    return "Map [id=" + id + ", name=" + name + ", description=" + description + ", teamSize=" + teamSize + ", prepareTime=" + prepareTime + ", lives=" + lives
        + ", goldStart=" + goldStart + ", goldPerTick=" + goldPerTick + "]";
  }
}
