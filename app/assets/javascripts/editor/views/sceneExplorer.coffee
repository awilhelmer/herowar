EditorEventbus = require 'editorEventbus'
BaseView = require 'views/baseView'
templates = require 'templates'
log = require 'util/logger'
db = require 'database'

class SceneExplorer extends BaseView

	id: 'scene-exlorer'

	className: 'sidebar-panel'

	template: templates.get 'sceneExplorer.tmpl'

	events:
		'click .scenegraph-tree .scenegraph-tree-world' : 'selectElement'
		'click .scenegraph-tree .scenegraph-tree-terrain' : 'selectElement'
		'click .scenegraph-tree .scenegraph-tree-folder' : 'selectElement'

	initialize: (options) ->
		@sidebar = db.get 'ui/sidebar'
		super options

	bindEvents: ->
		EditorEventbus.selectWorldViewport.add @selectWorld
		EditorEventbus.selectTerrainViewport.add @selectTerrain

	selectElement: (event) ->
		$currentTarget = $ event.currentTarget
		type = $currentTarget.data 'type'
		switch type
			when 'world'
				EditorEventbus.dispatch 'selectWorldUI'
				@selectWorld()
			when 'terrain'
				EditorEventbus.dispatch 'selectTerrainUI'
				@selectTerrain()
			when 'environment'
				@sidebar.set 'active', 'sidebar-environment'
				@selectItem type
			when 'pathing'
				@sidebar.set 'active', 'sidebar-pathing'
				@selectItem type
			when 'waves'
				@sidebar.set 'active', 'sidebar-waves'
				@selectItem type
			else
				log.error "ERROR type \"#{type}\" is unknowned"

	selectItem: (type) ->
		@$el.find('.scenegraph-tree div').removeClass 'active'
		@$el.find(".scenegraph-tree-#{type}").addClass 'active'

	selectWorld: =>
		@selectItem 'world'
		@sidebar.set 'active', 'sidebar-properties-world'

	selectTerrain: =>
		@selectItem 'terrain'
		@sidebar.set 'active', 'sidebar-properties-terrain'

return SceneExplorer