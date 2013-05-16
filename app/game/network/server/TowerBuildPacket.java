package game.network.server;

import game.network.BasePacket;
import game.network.PacketType;
import models.entity.game.Vector3;

/**
 * The TowerBuildPacket will be send from server to client after a player build
 * somewhere a tower.
 * 
 * @author Sebastian Sachtleben
 */
@SuppressWarnings("serial")
public class TowerBuildPacket extends BasePacket {

  protected long objectId;
  protected long towerId;
  protected long playerId;
  protected Vector3 position;
  
  public TowerBuildPacket(long objectId, long towerId, long playerId, Vector3 position) {
    super();
    this.type = PacketType.TowerBuildPacket;
    this.objectId = objectId;
    this.towerId = towerId;
    this.playerId = playerId;
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

  public Vector3 getPosition() {
    return position;
  }

  public void setPosition(Vector3 position) {
    this.position = position;
  }

  @Override
  public String toString() {
    return "TowerBuildPacket [objectId=" + objectId + ", towerId=" + towerId + ", playerId=" + playerId + ", position=" + position + ", type=" + type
        + ", createdTime=" + createdTime + "]";
  }
}
