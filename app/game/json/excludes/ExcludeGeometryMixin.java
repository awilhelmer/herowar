package game.json.excludes;

import org.codehaus.jackson.annotate.JsonIgnore;

import models.entity.game.Geometry;

/**
 * Mix-In class entity to json but exclude the geometry object.
 * 
 * @author Sebastian Sachtleben
 */
public class ExcludeGeometryMixin {

  @JsonIgnore
  private Geometry geometry;
  
}
