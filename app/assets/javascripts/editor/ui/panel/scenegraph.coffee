BasePanel = require 'ui/panel/basePanel'
TerrainProperties = require 'ui/panel/terrain'
	
class Scenegraph extends BasePanel
	
	constructor: (@app) ->
		super @app, 'scenegraph'

	initialize: ->
		console.log 'Initialize editor scenegraph'
		@terrain = new TerrainProperties @app
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
			$('#sidebar-properties-world, #sidebar-properties-terrain, #sidebar-properties-object').addClass 'hidden'
			propWindow = $ '#sidebar-properties-' + type
			propWindow.removeClass 'hidden' if propWindow
	
return Scenegraph