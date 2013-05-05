package game.json.excludes;

import java.util.Set;

import org.codehaus.jackson.annotate.JsonIgnore;

import models.entity.game.Material;
import models.entity.game.Mesh;
import models.entity.game.Terrain;
import models.entity.game.Wave;

public class MapDataExcludeMixin {
  @JsonIgnore
  private Terrain terrain;
  @JsonIgnore
  private Set<Wave> waves;
  @JsonIgnore
  private Set<Mesh> objects;
  @JsonIgnore
  private Set<Material> allMaterials;
}
