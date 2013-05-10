package game.network.server;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Contains preload data for PreloadDataPacket.
 * 
 * @author Sebastian Sachtleben
 */
@SuppressWarnings("serial")
public class PreloadData implements Serializable {
  
  private Map<String, String> textures = new HashMap<String, String>();
  private Map<String, String> texturesCube = new HashMap<String, String>();
  private Map<String, String> geometries = new HashMap<String, String>();
  
  public PreloadData(Map<String, String> textures, Map<String, String> texturesCube, Map<String, String> geometries) {
    this.textures = textures;
    this.texturesCube = texturesCube;
    this.geometries = geometries;
  }
  
  public PreloadData() {
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

  public Map<String, String> getGeometries() {
    return geometries;
  }

  public void setGeometries(Map<String, String> geometries) {
    this.geometries = geometries;
  }
}
