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

  protected Integer lives;
  protected Long gold;
  
  public PlayerStatsUpdatePacket(Integer lives, Long gold) {
    super();
    this.type = PacketType.PlayerStatsUpdatePacket;
    this.lives = lives;
    this.gold = gold;
  }

  public Integer getLives() {
    return lives;
  }

  public void setLives(Integer lives) {
    this.lives = lives;
  }

  public Long getGold() {
    return gold;
  }

  public void setGold(Long gold) {
    this.gold = gold;
  }

  @Override
  public String toString() {
    return "PlayerStatsUpdatePacket [type=" + type + ", createdTime=" + createdTime + ", lives=" + lives + ", gold=" + gold + "]";
  }
}
