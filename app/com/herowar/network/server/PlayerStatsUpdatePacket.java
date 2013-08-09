package com.herowar.network.server;

import com.herowar.network.BasePacket;
import com.herowar.network.PacketType;

/**
 * Server sends player stats to client to syncronize values.
 * 
 * @author Sebastian Sachtleben
 */
@SuppressWarnings("serial")
public class PlayerStatsUpdatePacket extends BasePacket {

	protected Long score;
	protected Long lives;
	protected Long gold;
	protected Integer changedScore;
	protected Integer changedLives;
	protected Integer changedGold;

	public PlayerStatsUpdatePacket(Long score, Long lives, Long gold, Integer changedScore, Integer changedLives, Integer changedGold) {
		super();
		this.type = PacketType.PlayerStatsUpdatePacket;
		this.score = score;
		this.lives = lives;
		this.gold = gold;
		this.changedScore = changedScore;
		this.changedLives = changedLives;
		this.changedGold = changedGold;
	}

	public Long getScore() {
		return score;
	}

	public void setScore(Long score) {
		this.score = score;
	}

	public Long getLives() {
		return lives;
	}

	public void setLives(Long lives) {
		this.lives = lives;
	}

	public Long getGold() {
		return gold;
	}

	public void setGold(Long gold) {
		this.gold = gold;
	}

	public Integer getChangedScore() {
		return changedScore;
	}

	public void setChangedScore(Integer changedScore) {
		this.changedScore = changedScore;
	}

	public Integer getChangedLives() {
		return changedLives;
	}

	public void setChangedLives(Integer changedLives) {
		this.changedLives = changedLives;
	}

	public Integer getChangedGold() {
		return changedGold;
	}

	public void setChangedGold(Integer changedGold) {
		this.changedGold = changedGold;
	}

	@Override
	public String toString() {
		return "PlayerStatsUpdatePacket [type=" + type + ", createdTime=" + createdTime + ", lives=" + lives + ", gold=" + gold + "]";
	}
}
