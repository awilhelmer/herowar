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

	bindEvents: ->
		EditorEventbus.selectWorldViewport.add @selectWorld
		EditorEventbus.selectTerrainViewport.add @selectTerrain
		# EditorEventbus.selectObjectViewport.add @selectObject

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
				EditorEventbus.dispatch 'showSidebarEnvironment'
				@selectItem type
			when 'pathing'
				EditorEventbus.dispatch 'showSidebarPathing'
				@selectItem type
			else
				log.error "ERROR type \"#{type}\" is unknowned"

	selectItem: (type) ->
		@$el.find('.scenegraph-tree div').removeClass 'active'
		@$el.find(".scenegraph-tree-#{type}").addClass 'active'

	selectWorld: =>
		@selectItem 'world'
		EditorEventbus.dispatch 'showWorldProperties'

	selectTerrain: =>
		@selectItem 'terrain'
		EditorEventbus.dispatch 'showTerrainProperties'

return SceneExplorer