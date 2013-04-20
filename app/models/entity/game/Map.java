package models.entity.game;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * @author Sebastian Sachtleben
 */
@Entity
@SuppressWarnings("serial")
public class Map implements Serializable {

  @Id
  private Long id;

  private String name;
  private String description;
  private String skybox;
  private Integer teamSize;
  private Integer prepareTime;
  private Integer lives;
  private Integer goldStart;
  private Integer goldPerTick;

  @OneToOne
  private Terrain terrain;

  @OneToMany(cascade = CascadeType.ALL)
  private List<Wave> waves;

//  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy="id.mat")
//  @JsonIgnore
//  private Set<MapMaterials> mapMaterials;
  
  // For JSON mapping ...
//  private List<Material> materials;


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
    this.getTerrain().getGeometry().setMetadata(new GeoMetaData());
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

  public List<Wave> getWaves() {
    return waves;
  }

  public void setWaves(List<Wave> waves) {
    this.waves = waves;
  }

//  public Set<MapMaterials> getMapMaterials() {
//    return mapMaterials;
//  }
//
//  public void setMapMaterials(Set<MapMaterials> mapMaterials) {
//    this.mapMaterials = mapMaterials;
//  }

//  @Transient
//  public List<Material> getMaterials() {
//    return materials;
//  }
//
//  public void setMaterials(List<Material> materials) {
//    this.materials = materials;
//  }


  @Override
  public String toString() {
    return "Map [id=" + id + ", name=" + name + ", description=" + description + ", teamSize=" + teamSize + ", prepareTime=" + prepareTime + ", lives=" + lives
        + ", goldStart=" + goldStart + ", goldPerTick=" + goldPerTick + "]";
  }
}
