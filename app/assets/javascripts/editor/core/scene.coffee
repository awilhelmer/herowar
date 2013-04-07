EditorEventbus = require 'editorEventbus'
RandomPool = require 'helper/randomPool'
TerrainModel = require 'model/terrain'
Constants = require 'constants'

class Scene

	constructor: (@editor) ->
		@initialize()
	
	initialize: ->
		console.log 'Initialize scene'
		EditorEventbus.worldAdded.dispatch
		EditorEventbus.terrainAdded.dispatch
		@randomPool = new RandomPool()
		@randomPool.hook()
		@model = new TerrainModel()
		@reset()

	reset: ->
		console.log 'Reseting scene'
		@editor.engine.scenegraph.addSkybox 'default'
		@terrain =
			width				: Constants.TERRAIN_DEFAULT_WIDTH
			height			: Constants.TERRAIN_DEFAULT_HEIGHT
			smoothness	: Constants.TERRAIN_DEFAULT_SMOOTHNESS
			zScale			: Constants.TERRAIN_DEFAULT_ZSCALE
		@resetTerrainPool()

	buildTerrain: ->
		console.log "Change terrain: size=#{@terrain.width}x#{@terrain.height} smoothness=#{@terrain.smoothness} zscale=#{@terrain.zScale}"
		@randomPool.seek 0
		map = @model.update @terrain.width, @terrain.height, @terrain.smoothness, @terrain.zScale
		@editor.engine.scenegraph.setMap map
		@editor.engine.render()

	resetTerrainPool: ->
		@randomPool.reset()
		@buildTerrain()

return Scene