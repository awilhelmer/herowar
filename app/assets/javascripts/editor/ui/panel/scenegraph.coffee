BasePanel = require 'ui/panel/basePanel'
ObjectHelper = require 'helper/objectHelper'
ObjectProperties = require 'ui/panel/object'
TerrainProperties = require 'ui/panel/terrain'
WorldProperties = require 'ui/panel/world'
	
class Scenegraph extends BasePanel
	
	constructor: (@editor) ->
		super @editor, 'scenegraph'

	initialize: ->
		console.log 'Initialize editor scenegraph'
		@objectHelper = new ObjectHelper @editor
		@subPanels = []
		@subPanels.push @object = new ObjectProperties @editor
		@subPanels.push @terrain = new TerrainProperties @editor
		@subPanels.push @world = new WorldProperties @editor
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
			@editor.render()

	handleSelection: (obj) ->
		if @objectHelper.isTerrain obj
			@selectElement $('.scenegraph-tree-terrain')
		else
			@selectElement $('.scenegraph-tree-world') # TODO: add object selection logic

return Scenegraph