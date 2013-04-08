ObjectHelper = require 'helper/objectHelper'
Constants = require 'constants'

class Terrain extends Backbone.Model

	initialize: (options) ->
		@reset()

	reset: ->
		@set 
			'name'						: 'Terrain'
			'width' 					: Constants.TERRAIN_DEFAULT_WIDTH
			'widthMin' 				: Constants.TERRAIN_MIN_WIDTH
			'widthMax' 				: Constants.TERRAIN_MAX_WIDTH
			'widthSteps' 			: Constants.TERRAIN_STEPS_WIDTH
			'height' 					: Constants.TERRAIN_DEFAULT_HEIGHT
			'heightMin' 			: Constants.TERRAIN_MIN_HEIGHT
			'heightMax' 			: Constants.TERRAIN_MAX_HEIGHT
			'heightSteps' 		: Constants.TERRAIN_STEPS_HEIGHT
			'smoothness' 			: Constants.TERRAIN_DEFAULT_SMOOTHNESS
			'smoothnessMin' 	: Constants.TERRAIN_MIN_SMOOTHNESS
			'smoothnessMax' 	: Constants.TERRAIN_MAX_SMOOTHNESS
			'smoothnessSteps'	: Constants.TERRAIN_STEPS_SMOOTHNESS
			'zScale' 					: Constants.TERRAIN_DEFAULT_ZSCALE
			'zScaleMin' 			: Constants.TERRAIN_MIN_ZSCALE
			'zScaleMax' 			: Constants.TERRAIN_MAX_ZSCALE
			'zScaleSteps' 		: Constants.TERRAIN_STEPS_ZSCALE
			'color'						: Constants.TERRAIN_DEFAULT_COLOR
			'wireframe'				: Constants.TERRAIN_DEFAULT_WIREFRAME
		@setMaterials()		

	setMaterials: ->
		@set 'materials', [ new THREE.MeshBasicMaterial color: @get('color') ]

	update: ->
		segWidth = Math.round(@get('width') / 10)
		segHeight = Math.round(@get('height') / 10)
		model = generateTerrain segWidth, segHeight, @get 'smoothness'
		zScale = @get 'zScale'
		terrain = []
		for i in [0..segHeight]
			row = []
			for j in [0..segWidth]
				row.push model[i][j] * zScale
			terrain.push row
		@getTerrainMesh terrain, segWidth, segHeight

	getTerrainMesh: (terrain, segWidth, segHeight) ->		
		obj = THREE.SceneUtils.createMultiMaterialObject new THREE.PlaneGeometry(@get('width'), @get('height'), segWidth, segHeight), @get('materials')
		obj.name = @get 'name'
		for mesh in obj.children
			mesh.rotation.x = - Math.PI/2
			for i in [0..segHeight]
				for j in [0..segWidth]
					vector = mesh.geometry.vertices[i * segHeight + j]
					vector.z = terrain[i][j] if vector
		obj

return Terrain