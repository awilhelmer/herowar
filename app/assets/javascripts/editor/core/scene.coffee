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
		@objectHelper = new ObjectHelper @editor
		@randomPool = new RandomPool()
		@randomPool.hook()
		@world = db.get 'world'
		@terrain = db.get 'terrain'
		@addEventListeners()
		@newMapEmpty()

	addEventListeners: ->
		EditorEventbus.changeTerrain.add @changeTerrain
		EditorEventbus.changeTerrainWireframe.add @changeTerrainWireframe
		EditorEventbus.resetTerrainPool.add @resetTerrainPool
		EditorEventbus.newMapEmpty.add @newMapEmpty

	newMapEmpty: =>
		console.log 'Initialize scene'
		EditorEventbus.worldAdded.dispatch
		EditorEventbus.terrainAdded.dispatch
		@reset()		
		@createTextures()
		
	createTextures: ->
		@textures = db.get 'textures'
		nextTextureId = 1
		@textures.add @createTexture nextTextureId++, 'Blank', '', undefined
		@textures.add @createTexture nextTextureId++, key, val.sourceFile, val for key, val of @editor.data.textures

	createTexture: (id, name, sourceFile, threeTexture) ->
		texture = new Texture()
		texture.set
			'id'						: id
			'name' 					: name
			'sourceFile'		: sourceFile
			'threeTexture'	: threeTexture
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
		col.add new Material 1, 1, 'Terrain', '#006600'
		@terrain.addMaterial 1

return Scene