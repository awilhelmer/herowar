package models.entity.game;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import models.entity.BaseModel;

import org.hibernate.annotations.Type;

/**
 * The match entity represents a played match in our application.
 * 
 * @author Sebastian Sachtleben
 */
@Entity
@Table(name = "matches")
@SuppressWarnings("serial")
public class Match extends BaseModel {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Long preloadTime;
  private Long gameTime;
  private Integer lives;

  @Type(type = "yes_no")
  private Boolean victory;

  @Enumerated(EnumType.STRING)
  private MatchState state = MatchState.INIT;

  @ManyToOne(cascade = { CascadeType.REFRESH }, optional = false)
  @JoinColumn(name = "map_id")
  private Map map;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "match")
  private Set<MatchResult> playerResults = new HashSet<MatchResult>();

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getPreloadTime() {
    return preloadTime;
  }

  public void setPreloadTime(Long preloadTime) {
    this.preloadTime = preloadTime;
  }

  public Long getGameTime() {
    return gameTime;
  }

  public void setGameTime(Long gameTime) {
    this.gameTime = gameTime;
  }

  public Integer getLives() {
    return lives;
  }

  public void setLives(Integer lives) {
    this.lives = lives;
  }

  public Boolean getVictory() {
    return victory;
  }

  public void setVictory(Boolean victory) {
    this.victory = victory;
  }

  public MatchState getState() {
    return state;
  }

  public void setState(MatchState state) {
    this.state = state;
  }

  public Map getMap() {
    return map;
  }

  public void setMap(Map map) {
    this.map = map;
  }

  public Set<MatchResult> getPlayerResults() {
    return playerResults;
  }

  public void setPlayerResults(Set<MatchResult> playerResults) {
    this.playerResults = playerResults;
  }
}
