package game.network.server;

import game.network.PacketType;

/**
 * Server sends first time info about the player stats.
 * 
 * @author Sebastian Sachtleben
 */
@SuppressWarnings("serial")
public class PlayerStatsInitPacket extends PlayerStatsUpdatePacket {

  protected Integer goldPerTick;

  public PlayerStatsInitPacket(Integer lives, Long gold, Integer goldPerTick) {
    super(lives, gold);
    this.type = PacketType.PlayerStatsInitPacket;
    this.goldPerTick = goldPerTick;
  }

  public Integer getGoldPerTick() {
    return goldPerTick;
  }

  public void setGoldPerTick(Integer goldPerTick) {
    this.goldPerTick = goldPerTick;
  }

  @Override
  public String toString() {
    return "PlayerStatsInitPacket [type=" + type + ", createdTime=" + createdTime + ", lives=" + lives + ", gold=" + gold + ", goldPerTick=" + goldPerTick
        + "]";
  }
}
