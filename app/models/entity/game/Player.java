package models.entity.game;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.codehaus.jackson.annotate.JsonIgnore;

import models.entity.User;

@Entity
@SuppressWarnings("serial")
public class Player implements Serializable {

	@Id
	@OneToOne
	@JsonIgnore
	private User user;
	private Long level = 1L;
	private Long experience = 0L;
	private Long wins = 0L;
	private Long losses = 0L;
	private Long kills = 0L;
	private Long assists = 0L;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = { CascadeType.ALL })
  @JsonIgnore
  private Set<GameResult> gameResults;

	private Player() {
	  gameResults = new HashSet<GameResult>();
	}
	
	public Player(User user) {
	  this();
	  this.user = user;
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
	
  public Set<GameResult> getGameResults() {
    return gameResults;
  }

  public void setGameResults(Set<GameResult> gameResults) {
    this.gameResults = gameResults;
  }
}
