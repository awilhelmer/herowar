package com.herowar.game.json.excludes;

import java.util.Set;


import org.codehaus.jackson.annotate.JsonIgnore;

import com.herowar.models.entity.game.Material;
import com.herowar.models.entity.game.Mesh;
import com.herowar.models.entity.game.Path;
import com.herowar.models.entity.game.Terrain;
import com.herowar.models.entity.game.Tower;
import com.herowar.models.entity.game.Wave;

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
	private Set<Tower> towers;

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
