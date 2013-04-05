BasePanel = require 'ui/panel/basePanel'
ObjectHelper = require 'helper/objectHelper'
ObjectProperties = require 'ui/panel/object'
TerrainProperties = require 'ui/panel/terrain'
WorldProperties = require 'ui/panel/world'
	
class Scenegraph extends BasePanel
	
	constructor: (@app) ->
		super @app, 'scenegraph'

	initialize: ->
		console.log 'Initialize editor scenegraph'
		@objectHelper = new ObjectHelper @app
		@subPanels = []
		@subPanels.push @object = new ObjectProperties @app
		@subPanels.push @terrain = new TerrainProperties @app
		@subPanels.push @world = new WorldProperties @app
		@selectElement @$container.find('.scenegraph-tree-world')
		@activeType = 'world'
		super()

	bindEvents: ->
		@$container.on 'click', '.scenegraph-tree div', @selectElementClick		

	selectElementClick: (event) =>
		if event
			event.preventDefault()
			@selectElement $(event.currentTarget)

	selectElement: ($Target) =>
		@$container.find('.scenegraph-tree div').removeClass 'active'
		$Target.addClass 'active'
		type = $Target.data 'type'
		if type
			panel.hide() for panel in @subPanels
			@[type].show()
			oldType = @activeType
			@activeType = type
			@setWireframe oldType, @activeType

	setWireframe: (oldType, newType) ->
		if oldType is 'terrain'
			console.log 'Old type was terrain'
			map = @app.scenegraph().getMap()
			if @terrain.wireframe
				@objectHelper.changeWireframeColor map, 0xFFFFFF
			else
				@objectHelper.removeWireframe map
		if newType is 'terrain'
			console.log 'New type is terrain'
			map = @app.scenegraph().getMap()
			if @terrain.wireframe
				@objectHelper.changeWireframeColor map, 0xFFFF00
			else
				@objectHelper.addWireframe map, 0xFFFF00
		@app.render()

return Scenegraph