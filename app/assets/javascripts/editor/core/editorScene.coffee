ObjectHelper = require 'helper/objectHelper'
RandomPool = require 'helper/randomPool'
materialHelper = require 'helper/materialHelper'
EditorEventbus = require 'editorEventbus'
Constants = require 'constants'
Scene = require 'core/scene'
log = require 'util/logger'
db = require 'database'

class EditorScene extends Scene
	
	initialize: ->
		@objectHelper = new ObjectHelper @app
		@randomPool = new RandomPool()
		@randomPool.hook()
		@addEventListeners()

	addEventListeners: ->
		EditorEventbus.changeTerrain.add @changeTerrain
		EditorEventbus.changeTerrainWireframe.add @changeTerrainWireframe
		EditorEventbus.resetTerrainPool.add @resetTerrainPool
		EditorEventbus.handleWorldMaterials.add @handleMaterials
		EditorEventbus.removeStaticObject.add @removeStaticObject

	reset: =>
		@resetTerrainPool()
		super()

	changeTerrain: (width, height, smoothness, zScale) =>
		width = parseInt width
		height = parseInt height
		smoothness = parseFloat smoothness
		zScale = parseInt zScale
		if @hasValidSize(width, height) and @hasChangedSize(width, height, smoothness, zScale)
			log.debug 'Terrain has valid changes'
			@world.get('terrain').width = width
			@world.get('terrain').height = height
			@world.get('terrain').smoothness = smoothness
			@world.get('terrain').zScale = zScale
			@buildTerrain()

	changeTerrainWireframe: (value) =>
		@world.get('terrain').wireframe = value

	resetTerrainPool: =>
		log.info 'Reseting terrain pool'
		@randomPool.reset()

	buildTerrain: (map) =>
		log.info "Change terrain: size=#{@world.get('terrain').width}x#{@world.get('terrain').height} smoothness=#{@world.get('terrain').smoothness} zscale=#{@world.get('terrain').zScale}"
		@randomPool.seek 0
		unless @world.get('terrain').geometry instanceof THREE.Geometry
			map = @world.terrainUpdate()
			@app.engine.scenegraph.setMap map
		map = super map
		@world.get('terrain').geometry.faces = map.children[0].geometry.faces
		@world.get('terrain').geometry.vertices = map.children[0].geometry.vertices
		@objectHelper.addWireframe map, @getWireframeColor() if !@objectHelper.hasWireframe(map) or @world.get('terrain').wireframe
		@app.engine.render()

	getWireframeColor: =>
		if @isInitialized then 0xFFFF00 else 0xFFFFFF

	hasValidSize: (width, height) =>
		width >= Constants.TERRAIN_MIN_WIDTH and width <= Constants.TERRAIN_MAX_WIDTH and height >= Constants.TERRAIN_MIN_HEIGHT and height <= Constants.TERRAIN_MAX_HEIGHT

	hasChangedSize: (width, height, smoothness, zScale) =>
		width != parseInt(@world.get('terrain').width) or height != parseInt(@world.get('terrain').height) or smoothness != parseFloat(@world.get('terrain').smoothness) or zScale != parseInt(@world.get('terrain').zScale)

	handleMaterials: =>
		@world.handleMaterials @app.engine.scenegraph.getMap()
	
	removeStaticObject: (obj) =>
		log.info "Environment \"#{obj.get('name')}\" removed"
		col = db.get 'environmentsStatic'
		col.remove obj
		@app.engine.scenegraph.removeStaticObject obj.attributes
		EditorEventbus.dispatch 'render'

	afterCreatingPaths: (wayId, pathId) ->
		@app.tools.addWaypoint.nextId = wayId
		EditorEventbus.dispatch 'initIdChanged', 'pathing', pathId

	afterCreatingWaves: (waveId) ->
		EditorEventbus.dispatch 'initIdChanged', 'waves', waveId

return EditorScene