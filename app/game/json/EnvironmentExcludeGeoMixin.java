package game.json;

import org.codehaus.jackson.annotate.JsonIgnore;

import models.entity.game.Geometry;

/**
 * Mix-In class for Environment entity to json but exclude the geometry object.
 * 
 * @author Sebastian Sachtleben
 */
public class EnvironmentExcludeGeoMixin {

  @JsonIgnore
  private Geometry geometry;
  
}
