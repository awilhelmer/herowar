package com.herowar.json.excludes;


import org.codehaus.jackson.annotate.JsonIgnore;

import com.herowar.models.entity.game.Geometry;

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
