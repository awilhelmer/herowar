EditorEventbus = require 'editorEventbus'
ObjectHelper = require 'helper/objectHelper'
RandomPool = require 'helper/randomPool'
TerrainModel = require 'models/terrain'
Material = require 'models/material'
Texture = require 'models/texture'
Constants = require 'constants'
db = require 'database'

class Scene

	constructor: (@editor) ->
		@isInitialized = false
		@initialize()
		@isInitialized = true
	
	initialize: ->
		console.log 'Initialize scene'
		EditorEventbus.worldAdded.dispatch
		EditorEventbus.terrainAdded.dispatch
		@objectHelper = new ObjectHelper @editor
		@randomPool = new RandomPool()
		@randomPool.hook()
		@world = db.get 'world'
		@terrain = db.get 'terrain'
		@reset()
		@addEventListeners()
		@createTextures()

	addEventListeners: ->
		EditorEventbus.changeTerrain.add @changeTerrain
		EditorEventbus.changeTerrainWireframe.add @changeTerrainWireframe
		EditorEventbus.resetTerrainPool.add @resetTerrainPool

	createTextures: ->
		@textures = db.get 'textures'
		@textures.add @createTexture 1, 'Blank', ''
		@textures.add @createTexture 2, 'Stone Natural 001', 'assets/images/game/textures/stone/natural-001.jpg'
		@textures.add @createTexture 3, 'Stone Rough 001', 'assets/images/game/textures/stone/rough-001.jpg'

	createTexture: (id, name, path) ->
		texture = new Texture()
		texture.set
			'id'		: id
			'name' 	: name
			'path'	: path
		texture

	changeTerrain: (width, height, smoothness, zScale) =>
		if @hasValidSize(width, height) and @hasChangedSize(parseInt(width), parseInt(height), parseFloat(smoothness), parseInt(zScale))
			console.log 'Terrain has valid changes'
			@terrain.set
				'width' 			: width
				'height' 			: height
				'smoothness' 	: smoothness
				'zScale' 			: zScale
			@buildTerrain()

	changeTerrainWireframe: (value) =>
		@terrain.set 'wireframe', value

	resetTerrainPool: =>
		console.log 'Reseting terrain pool'
		@randomPool.reset()
		@buildTerrain()

	reset: =>
		console.log 'Reseting scene'
		@world.reset()
		@terrain.reset()
		@createTerrainMaterial()
		@editor.engine.scenegraph.addSkybox @world.get 'skybox'
		@resetTerrainPool()

	buildTerrain: =>
		console.log "Change terrain: size=#{@terrain.get('width')}x#{@terrain.get('height')} smoothness=#{@terrain.get('smoothness')} zscale=#{@terrain.get('zScale')}"
		@randomPool.seek 0
		map = @terrain.update()
		@objectHelper.addWireframe map, @getWireframeColor() if !@objectHelper.hasWireframe(map) or @terrain.get 'wireframe'
		@editor.engine.scenegraph.setMap map
		@editor.engine.render()

	getWireframeColor: =>
		if @isInitialized then 0xFFFF00 else 0xFFFFFF

	hasValidSize: (width, height) =>
		width >= Constants.TERRAIN_MIN_WIDTH and width <= Constants.TERRAIN_MAX_WIDTH and height >= Constants.TERRAIN_MIN_HEIGHT and height <= Constants.TERRAIN_MAX_HEIGHT

	hasChangedSize: (width, height, smoothness, zScale) =>
		width != parseInt(@terrain.get('width')) or height != parseInt(@terrain.get('height')) or smoothness != parseFloat(@terrain.get('smoothness')) or zScale != parseInt(@terrain.get('zScale'))

	createTerrainMaterial: ->
		col = db.get 'materials'
		mat = new Material()
		mat.set 
			'id'					: 1
			'materialId' 	: 1
			'name'				: 'Terrain'
			'color' 			: '#006600'
		col.add mat
		@terrain.addMaterial 1

return Scene