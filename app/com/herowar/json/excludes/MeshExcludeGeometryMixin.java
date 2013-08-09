package com.herowar.json.excludes;


import org.codehaus.jackson.annotate.JsonIgnore;

import com.herowar.models.entity.game.Geometry;

/**
 * Mix-In class entity to json but exclude the geometry property for mesh object.
 * 
 * @author Alexander Wilhelmer
 */
public class MeshExcludeGeometryMixin {

	@SuppressWarnings("unused")
	@JsonIgnore
	private Geometry geometry;

}
