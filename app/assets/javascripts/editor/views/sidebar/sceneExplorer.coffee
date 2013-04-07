EditorEventbus = require 'editorEventbus'
BaseView = require 'views/baseView'
templates = require 'templates'
db = require 'database'

class SceneExplorer extends BaseView
	
	id: 'sidebar-sceneexlorer'
	
	className: 'sidebar-panel'
	
	template: templates.get 'sidebar/sceneExplorer.tmpl'

	events:
		'click .scenegraph-tree div' 	: 'selectElement'

	initialize: (options) ->
		@world = false
		@terrain = false
		@activeType = 'world'
		super options

	bindEvents: ->
		EditorEventbus.worldAdded.add @worldAdded
		EditorEventbus.terrainAdded.add @terrainAdded
		EditorEventbus.selectWorldViewport.add @selectWorld
		EditorEventbus.selectTerrainViewport.add @selectTerrain
		EditorEventbus.selectObjectViewport.add @selectObject
	
	render: ->
		super()
		# TODO: handle this with a model
		@$el.find('.scenegraph-tree').append '<div class="scenegraph-tree-world active" data-type="world"><img src="assets/images/editor/world.png" /><span>World</span></div>'
		@$el.find('.scenegraph-tree').append '<div class="scenegraph-tree-terrain" data-type="terrain"><img src="assets/images/editor/terrain.jpg" /><span>Terrain</span></div>'
	
	worldAdded: =>
		@world = true

	terrainAdded: =>
		@terrain = true
	
	selectElement: (event) ->
		@$el.find('.scenegraph-tree div').removeClass 'active'
		$currentTarget = $ event.currentTarget
		$currentTarget.addClass 'active'
		type = $currentTarget.data 'type'
		if type
			db.get(@activeType).removeSelectionWireframe() if @activeType
			db.get(type).addSelectionWireframe()

	selectWorld: =>
		@$el.find('.scenegraph-tree div').removeClass 'active'
		@$el.find('.scenegraph-tree-world').addClass 'active'

	selectTerrain: =>
		@$el.find('.scenegraph-tree div').removeClass 'active'
		@$el.find('.scenegraph-tree-terrain').addClass 'active'

	selectObject: =>
		@$el.find('.scenegraph-tree div').removeClass 'active'
		@$el.find('.scenegraph-tree-object').addClass 'active'

return SceneExplorer