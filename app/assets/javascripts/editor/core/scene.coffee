ObjectHelper = require 'helper/objectHelper'
RandomPool = require 'helper/randomPool'
EditorEventbus = require 'editorEventbus'
MapProperties = require 'mapProperties'
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
		@addEventListeners()
		@reset()		
		@createTextures()

	addEventListeners: ->
		EditorEventbus.changeTerrain.add @changeTerrain
		EditorEventbus.changeTerrainWireframe.add @changeTerrainWireframe
		EditorEventbus.resetTerrainPool.add @resetTerrainPool

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
		width = parseInt width
		height = parseInt height
		smoothness = parseFloat smoothness
		zScale = parseInt zScale
		if @hasValidSize(width, height) and @hasChangedSize(width, height, smoothness, zScale)
			console.log 'Terrain has valid changes'
			@world.get('terrain').width = width
			@world.get('terrain').height = height
			@world.get('terrain').smoothness = smoothness
			@world.get('terrain').zScale = zScale
			@buildTerrain()

	changeTerrainWireframe: (value) =>
		@world.get('terrain').wireframe = value

	resetTerrainPool: =>
		console.log 'Reseting terrain pool'
		@randomPool.reset()
		@buildTerrain()

	reset: =>
		console.log 'Reseting scene'
		@createTerrainMaterial()
		@editor.engine.scenegraph.addSkybox @world.get 'skybox'
		@resetTerrainPool()

	buildTerrain: =>
		console.log "Change terrain: size=#{@world.get('terrain').width}x#{@world.get('terrain').height} smoothness=#{@world.get('terrain').smoothness} zscale=#{@world.get('terrain').zScale}"
		@randomPool.seek 0
		map = @world.terrainUpdate()
		@world.get('terrain').geometry.faces = map.children[0].geometry.faces
		@world.get('terrain').geometry.vertices = map.children[0].geometry.vertices
		@objectHelper.addWireframe map, @getWireframeColor() if !@objectHelper.hasWireframe(map) or @world.get('terrain').wireframe
		@editor.engine.scenegraph.setMap map
		@editor.engine.render()

	getWireframeColor: =>
		if @isInitialized then 0xFFFF00 else 0xFFFFFF

	hasValidSize: (width, height) =>
		width >= Constants.TERRAIN_MIN_WIDTH and width <= Constants.TERRAIN_MAX_WIDTH and height >= Constants.TERRAIN_MIN_HEIGHT and height <= Constants.TERRAIN_MAX_HEIGHT

	hasChangedSize: (width, height, smoothness, zScale) =>
		width != parseInt(@world.get('terrain').width) or height != parseInt(@world.get('terrain').height) or smoothness != parseFloat(@world.get('terrain').smoothness) or zScale != parseInt(@world.get('terrain').zScale)

	createTerrainMaterial: ->
		col = db.get 'materials'
		col.add new Material 1, 1, 'Terrain', '#006600'
		@world.addTerrainMaterial 1

return Scene