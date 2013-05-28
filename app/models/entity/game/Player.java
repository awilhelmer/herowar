package models.entity.game;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import models.entity.User;

@Entity
@SuppressWarnings("serial")
public class Player implements Serializable {

	@Id
	private User user;
	private Long level;
	private Long experience;
	private Long wins;
	private Long losses;
	private Long kills;
	private Long assists;

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
}
