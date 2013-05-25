package game.network.server;

import game.network.BasePacket;
import game.network.PacketType;

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
  protected Long changedScore;
  protected Long changedLives;
  protected Long changedGold;
  
  public PlayerStatsUpdatePacket(Long score, Long lives, Long gold, Long changedScore, Long changedLives, Long changedGold) {
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

  public Long getChangedScore() {
    return changedScore;
  }

  public void setChangedScore(Long changedScore) {
    this.changedScore = changedScore;
  }

  public Long getChangedLives() {
    return changedLives;
  }

  public void setChangedLives(Long changedLives) {
    this.changedLives = changedLives;
  }

  public Long getChangedGold() {
    return changedGold;
  }

  public void setChangedGold(Long changedGold) {
    this.changedGold = changedGold;
  }

  @Override
  public String toString() {
    return "PlayerStatsUpdatePacket [type=" + type + ", createdTime=" + createdTime + ", lives=" + lives + ", gold=" + gold + "]";
  }
}
