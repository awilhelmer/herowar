BasePanel = require 'ui/panel/basePanel'
ObjectHelper = require 'helper/objectHelper'

class TerrainPropertiesPanel extends BasePanel

	constructor: (@app) ->
		super @app, 'sidebar-properties-terrain'

	initialize: ->
		console.log 'Initialize editor terrain properties'
		@objectHelper = new ObjectHelper @app
		@wireframe = true
		super()

	bindEvents: ->
		@$container.on 'keyup', 'input[name="width"],input[name="height"]', @changeTerrainSize
		@$container.on 'change', 'input[name="wireframe"]', @changeWireframe
		
	changeTerrainSize: =>
		width = @$container.find('input[name="width"]').val()
		height = @$container.find('input[name="height"]').val()
		@app.scenegraph().setMap @app.scenegraph().createDefaultMap width, height
		@app.render()

	changeWireframe: (event) =>
		if event
			map = @app.scenegraph().getMap()
			$currentTarget = $ event.currentTarget
			@wireframe = $currentTarget.is ':checked'

	addSelectionWireframe: ->
		map = @app.scenegraph().getMap()
		if @wireframe
			@objectHelper.changeWireframeColor map, 0xFFFF00
		else
			@objectHelper.addWireframe map, 0xFFFF00

	removeSelectionWireframe: ->
		map = @app.scenegraph().getMap()
		if @wireframe
			@objectHelper.changeWireframeColor map, 0xFFFFFF
		else
			@objectHelper.removeWireframe map

return TerrainPropertiesPanel