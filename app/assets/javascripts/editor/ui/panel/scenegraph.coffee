BasePanel = require 'ui/panel/basePanel'
ObjectProperties = require 'ui/panel/object'
TerrainProperties = require 'ui/panel/terrain'
WorldProperties = require 'ui/panel/world'
	
class Scenegraph extends BasePanel
	
	constructor: (@app) ->
		super @app, 'scenegraph'

	initialize: ->
		console.log 'Initialize editor scenegraph'
		@subPanels = []
		@subPanels.push @object = new ObjectProperties @app
		@subPanels.push @terrain = new TerrainProperties @app
		@subPanels.push @world = new WorldProperties @app
		@selectElement @$container.find('.scenegraph-tree-world')
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

return Scenegraph