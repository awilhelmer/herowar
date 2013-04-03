package models.entity.game;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import models.entity.BaseModel;

/**
 * @author Sebastian Sachtleben
 */
@Entity
@SuppressWarnings("serial")
public class Map extends BaseModel {

  @Id
  private Long id;
  
  private String name;
  private String description;
  private Integer teamSize;
  private Integer prepareTime;
  private Integer lives;
  private Integer goldStart;
  private Integer goldPerTick;
  
  @OneToMany(cascade = CascadeType.ALL)
  private List<Wave> waves;
  
  private static final Finder<Long, Map> finder = new Finder<Long, Map>(Long.class, Map.class);

  public static void create(String name, String description, int teamSize) {
    final Map map = new Map();
    map.setName(name);
    map.setDescription(description);
    map.setTeamSize(teamSize);
    map.save();
  }
  
  public static void merge(final Map map, final Map map2) {
    map.setName(map2.getName());
    map.setDescription(map2.getDescription());
    map.setTeamSize(map2.getTeamSize());
    map.save();
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

  public List<Wave> getWaves() {
    return waves;
  }

  public void setWaves(List<Wave> waves) {
    this.waves = waves;
  }
  
  public static Finder<Long, Map> getFinder() {
    return finder;
  }
}
