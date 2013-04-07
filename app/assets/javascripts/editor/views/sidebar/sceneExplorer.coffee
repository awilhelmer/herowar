EditorEventbus = require 'editorEventbus'
BaseView = require 'views/baseView'
templates = require 'templates'

class SceneExplorer extends BaseView
	
	id: 'sidebar-sceneexlorer'
	
	className: 'sidebar-panel'
	
	template: templates.get 'sidebar/sceneExplorer.tmpl'
	
	initialize: (options) ->
		@world = false
		@terrain = false
		super options

	bindEvents: ->
		EditorEventbus.worldAdded.add @worldAdded
		EditorEventbus.terrainAdded.add @terrainAdded
	
	render: ->
		super()
		# TODO: handle this with a model
		@$el.find('.scenegraph-tree').append '<div class="scenegraph-tree-world active" data-type="world"><img src="assets/images/editor/world.png" /><span>World</span></div>'
		@$el.find('.scenegraph-tree').append '<div class="scenegraph-tree-terrain" data-type="terrain"><img src="assets/images/editor/terrain.jpg" /><span>Terrain</span></div>'
	
	worldAdded: =>
		@world = true

	terrainAdded: =>
		@terrain = true		

return SceneExplorer