EditorEventbus = require 'editorEventbus'
ObjectHelper = require 'helper/objectHelper'
RandomPool = require 'helper/randomPool'
TerrainModel = require 'model/terrain'
Constants = require 'constants'
db = require 'database'

class Scene

	constructor: (@editor) ->
		@initialize()
	
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

	reset: ->
		console.log 'Reseting scene'
		@world.reset()
		@terrain.reset()
		@editor.engine.scenegraph.addSkybox @world.get 'skybox'
		@resetTerrainPool()

	buildTerrain: ->
		console.log "Change terrain: size=#{@terrain.get('width')}x#{@terrain.get('height')} smoothness=#{@terrain.get('smoothness')} zscale=#{@terrain.get('zScale')}"
		@randomPool.seek 0
		map = @terrain.update()
		@objectHelper.addWireframe map, 0xFFFFFF if !@objectHelper.hasWireframe(map) and @terrain.get 'wireframe'
		@editor.engine.scenegraph.setMap map
		@editor.engine.render()

	resetTerrainPool: ->
		@randomPool.reset()
		@buildTerrain()

return Scene