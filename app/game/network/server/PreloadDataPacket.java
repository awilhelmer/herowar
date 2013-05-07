package game.network.server;

import java.util.HashMap;
import java.util.Map;

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
  private Map<String, String> textures = new HashMap<String, String>();
  private Map<String, String> texturesCube = new HashMap<String, String>();
  
  public PreloadDataPacket(Long map, Map<String, String> textures, Map<String, String> texturesCube) {
    this();
    this.map = map;
    this.textures = textures;
    this.texturesCube = texturesCube;
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

  public Map<String, String> getTextures() {
    return textures;
  }

  public void setTextures(Map<String, String> textures) {
    this.textures = textures;
  }

  public Map<String, String> getTexturesCube() {
    return texturesCube;
  }

  public void setTexturesCube(Map<String, String> texturesCube) {
    this.texturesCube = texturesCube;
  }

  @Override
  public String toString() {
    return "PreloadDataPacket [type=" + type + ", createdTime=" + createdTime + ", map=" + map + ", textures=" + textures + ", texturesCube=" + texturesCube  + "]";
  }
}
