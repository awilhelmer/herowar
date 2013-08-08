package com.herowar.game.network.server;

import com.herowar.game.models.TowerModel;
import com.herowar.game.network.BasePacket;
import com.herowar.game.network.PacketType;
import com.herowar.models.entity.game.Vector3;


/**
 * The TowerBuildPacket will be send from server to client after a player build somewhere a tower.
 * 
 * @author Sebastian Sachtleben
 */
@SuppressWarnings("serial")
public class TowerBuildPacket extends BasePacket {

	protected long objectId;
	protected long towerId;
	protected long playerId;
	protected String playerName;
	protected Vector3 position;

	public TowerBuildPacket(TowerModel tower, Vector3 position) {
		super();
		this.type = PacketType.TowerBuildPacket;
		this.objectId = tower.getId();
		this.towerId = tower.getDbId();
		this.playerId = tower.getConnection().id();
		this.playerName = tower.getConnection().user().getUsername();
		this.position = position;
	}

	public long getObjectId() {
		return objectId;
	}

	public void setObjectId(long objectId) {
		this.objectId = objectId;
	}

	public long getTowerId() {
		return towerId;
	}

	public void setTowerId(long towerId) {
		this.towerId = towerId;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public Vector3 getPosition() {
		return position;
	}

	public void setPosition(Vector3 position) {
		this.position = position;
	}

	@Override
	public String toString() {
		return "TowerBuildPacket [objectId=" + objectId + ", towerId=" + towerId + ", playerId=" + playerId + ", position=" + position
				+ ", type=" + type + ", createdTime=" + createdTime + "]";
	}
}
