package game.network.server;

import game.network.BasePacket;
import game.network.PacketType;

/**
 * Server sends a list of preload items.
 * 
 * @author Sebastian Sachtleben
 */
@SuppressWarnings("serial")
public class PreloadDataPacket extends BasePacket {

  private Long map;
  private PreloadData data;
  
  public PreloadDataPacket(Long map, PreloadData data) {
    this();
    this.map = map;
    this.data = data;
  }
  
  public PreloadDataPacket() {
    super();
    this.type = PacketType.PreloadDataPacket;
  }

  public Long getMap() {
    return map;
  }

  public void setMap(Long map) {
    this.map = map;
  }
  
  public PreloadData getData() {
    return data;
  }

  public void setData(PreloadData data) {
    this.data = data;
  }

  @Override
  public String toString() {
    return "PreloadDataPacket [type=" + type + ", createdTime=" + createdTime + ", map=" + map + ", data=" + data + "]";
  }
}
