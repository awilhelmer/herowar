package game.json.excludes;

import models.entity.game.Geometry;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * Mix-In class entity to json but exclude the geometry property for mesh
 * object.
 * 
 * @author Alexander Wilhelmer
 */
public class MeshExcludeGeometryMixin {

  @SuppressWarnings("unused")
  @JsonIgnore
  private Geometry geometry;

}
