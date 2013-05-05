package game.json.excludes;

import models.entity.game.Geometry;

import org.codehaus.jackson.annotate.JsonIgnore;

public class MeshExcludeGeometryMixin {
  @JsonIgnore
  private Geometry geometry;
}
