Constants = require 'constants'

mapProperties =

	MAP_ID								: 0
	MAP_TITLE							: ''
	MAP_DESCRIPTION				: ''
	MAP_TEAM_SIZE					: 1
	MAP_PREPARE_TIME			: 500
	MAP_LIVES							: 20
	MAP_GOLD_START				: 2000
	MAP_GOLD_PER_TICK			: 5

	TERRAIN_ID						: 0
	TERRAIN_WIDTH					: Constants.TERRAIN_DEFAULT_WIDTH
	TERRAIN_HEIGHT				: Constants.TERRAIN_DEFAULT_HEIGHT
	TERRAIN_SMOOTHNESS		: Constants.TERRAIN_DEFAULT_SMOOTHNESS
	TERRAIN_ZSCALE				: Constants.TERRAIN_DEFAULT_ZSCALE
	
	GEOMETRY_ID						: 0
	TERRAIN_FACES					: []
	TERRAIN_VERTICES			: []
	TERRAIN_MATERIALS			: []
	
	GEOMETRY_METADATA_ID	: 0

return mapProperties