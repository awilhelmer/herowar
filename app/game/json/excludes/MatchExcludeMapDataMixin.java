package game.json.excludes;

import java.util.Date;
import java.util.List;

import models.entity.game.Geometry;
import models.entity.game.Material;

import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * Mix-In class entity to json but exclude the map data properties.
 * 
 * @author Sebastian Sachtleben
 */
public class MatchExcludeMapDataMixin extends MapDataExcludeMixin {

	@JsonIgnore
	String description;

	@JsonIgnore
	String skybox;

	@JsonIgnore
	Integer teamSize;

	@JsonIgnore
	Integer prepareTime;

	@JsonIgnore
	Integer goldStart;

	@JsonIgnore
	Integer goldPerTick;

	@JsonIgnore
	Date cdate;

	@JsonIgnore
	Date udate;

	@JsonIgnore
	Long version;

	@JsonIgnore
	List<Material> materials;

	@JsonIgnore
	List<Geometry> staticGeometries;

}
