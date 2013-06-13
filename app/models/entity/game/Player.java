package models.entity.game;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.validation.constraints.NotNull;

import models.entity.User;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

@Entity
@SuppressWarnings("serial")
public class Player implements Serializable {

  @Id
  @GeneratedValue(generator = "playerGen")
  @GenericGenerator(name = "playerGen", strategy = "foreign", parameters = @Parameter(name = "property", value = "user"))
  private Long id;

  @OneToOne(mappedBy = "player")
  @NotNull
  @JsonIgnore
  private User user;

  private Long level = 1L;
  private Long experience = 0L;
  private Long wins = 0L;
  private Long losses = 0L;
  private Long kills = 0L;
  private Long assists = 0L;

  @OneToMany(fetch = FetchType.LAZY, mappedBy = "player", cascade = { CascadeType.ALL })
  @JsonIgnore
  private Set<MatchResult> matchResults = new HashSet<MatchResult>();

  @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @PrimaryKeyJoinColumn
  @JsonIgnore
  private PlayerSettings settings;

  // CONSTRUCTOR //

  public Player() {
    settings = new PlayerSettings(this);
  }

  public Player(User user) {
    this();
    this.user = user;
  }

  // GETTER & SETTER //

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Long getLevel() {
    return level;
  }

  public void setLevel(Long level) {
    this.level = level;
  }

  public Long getExperience() {
    return experience;
  }

  public void setExperience(Long experience) {
    this.experience = experience;
  }

  public Long getWins() {
    return wins;
  }

  public void setWins(Long wins) {
    this.wins = wins;
  }

  public Long getLosses() {
    return losses;
  }

  public void setLosses(Long losses) {
    this.losses = losses;
  }

  public Long getKills() {
    return kills;
  }

  public void setKills(Long kills) {
    this.kills = kills;
  }

  public Long getAssists() {
    return assists;
  }

  public void setAssists(Long assists) {
    this.assists = assists;
  }

  public Set<MatchResult> getMatchResults() {
    return matchResults;
  }

  public void setMatchResults(Set<MatchResult> matchResults) {
    this.matchResults = matchResults;
  }

  public PlayerSettings getSettings() {
    return settings;
  }

  public void setSettings(PlayerSettings settings) {
    this.settings = settings;
  }
}
