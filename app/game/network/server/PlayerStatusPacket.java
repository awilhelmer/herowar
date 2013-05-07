package game.network.server;

import game.network.BasePacket;
import game.network.PacketType;

/**
 * Server sends info about the current player status.
 * 
 * @author Sebastian Sachtleben
 */
@SuppressWarnings("serial")
public class PlayerStatusPacket extends BasePacket {

  private Integer lives;
  private Long gold;
  
  public PlayerStatusPacket(Integer lives, Long gold) {
    this();
    this.lives = lives;
    this.gold = gold;
  }
  
  public PlayerStatusPacket() {
    super();
    this.type = PacketType.PlayerStatusPacket;
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
    return "PlayerStatusPacket [type=" + type + ", createdTime=" + createdTime + ", lives=" + lives + ", gold=" + gold + "]";
  }
}
