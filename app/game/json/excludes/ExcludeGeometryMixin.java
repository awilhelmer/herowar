package game.json.excludes;

import models.entity.game.Geometry;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * Mix-In class entity to json but exclude the geometry object.
 * 
 * @author Sebastian Sachtleben
 */
public class ExcludeGeometryMixin {

  @SuppressWarnings("unused")
  @JsonIgnore
  private Geometry geometry;

}
