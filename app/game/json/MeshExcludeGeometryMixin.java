package game.json;

import models.entity.game.Geometry;

import org.codehaus.jackson.annotate.JsonIgnore;

public class MeshExcludeGeometryMixin {
  @JsonIgnore
  private Geometry geometry;
}
