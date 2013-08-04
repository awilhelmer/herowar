package game.network.server;

import game.models.UnitModel;
import game.network.PacketType;

/**
 * The UnitOutPacket will be send from server to client to tell a unit left the map.
 * 
 * @author Sebastian Sachtleben
 */
@SuppressWarnings("serial")
public class UnitOutPacket extends ObjectOutPacket {

	private Long killedBy = null;
	private Integer rewardGold = null;

	public UnitOutPacket(UnitModel unit) {
		super(unit.getId());
		this.type = PacketType.UnitOutPacket;
		this.position = unit.getTranslation();
		if (unit.getLastHitTower() != null) {
			this.killedBy = unit.getLastHitTower().getId();
			this.rewardGold = unit.getEntity().getRewardGold();
		}
	}

	public Long getKilledBy() {
		return killedBy;
	}

	public void setKilledBy(Long killedBy) {
		this.killedBy = killedBy;
	}

	public Integer getRewardGold() {
		return rewardGold;
	}

	public void setRewardGold(Integer rewardGold) {
		this.rewardGold = rewardGold;
	}
}
