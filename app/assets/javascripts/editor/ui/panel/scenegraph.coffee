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
		@$container.find('.scenegraph-tree').append '<div class="scenegraph-tree-world" data-type="world"><img src="assets/images/editor/world.png" /><span>World</span></div><div class="scenegraph-tree-terrain" data-type="terrain"><img src="assets/images/editor/terrain.jpg" /><span>Terrain</span></div>'
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
			@[@activeType].removeSelectionWireframe() if @activeType
			@[type].addSelectionWireframe()
			@[type].show()
			@activeType = type
			@app.render()

return Scenegraph