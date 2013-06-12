package game.json.excludes;

import java.util.Set;

import models.entity.game.Material;
import models.entity.game.Mesh;
import models.entity.game.Path;
import models.entity.game.Terrain;
import models.entity.game.Wave;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * Mix-In class entity to json but exclude the map data properties.
 * 
 * @author Alexander Wilhelmer
 */
public class MapDataExcludeMixin {

  @SuppressWarnings("unused")
  @JsonIgnore
  private Terrain terrain;

  @SuppressWarnings("unused")
  @JsonIgnore
  private Set<Wave> waves;

  @SuppressWarnings("unused")
  @JsonIgnore
  private Set<Mesh> objects;

  @SuppressWarnings("unused")
  @JsonIgnore
  private Set<Material> allMaterials;

  @SuppressWarnings("unused")
  @JsonIgnore
  private Set<Path> paths;

}
