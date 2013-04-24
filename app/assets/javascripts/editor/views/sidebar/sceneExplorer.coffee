EditorEventbus = require 'editorEventbus'
BaseView = require 'views/baseView'
templates = require 'templates'
db = require 'database'

class SceneExplorer extends BaseView

	id: 'sidebar-sceneexlorer'

	className: 'sidebar-panel'

	template: templates.get 'sidebar/sceneExplorer.tmpl'

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
				EditorEventbus.selectWorldUI.dispatch()
				@selectWorld()
			when 'terrain'
				EditorEventbus.selectTerrainUI.dispatch()
				@selectTerrain()
			when 'environment'
				EditorEventbus.showSidebarEnvironment.dispatch()
				@selectItem type
			when 'pathing'
				EditorEventbus.showSidebarPathing.dispatch()
				@selectItem type
			else
				console.log "ERROR type \"#{type}\" is unknowned"

	selectItem: (type) ->
		@$el.find('.scenegraph-tree div').removeClass 'active'
		@$el.find(".scenegraph-tree-#{type}").addClass 'active'

	selectWorld: =>
		@selectItem 'world'
		EditorEventbus.showWorldProperties.dispatch()

	selectTerrain: =>
		@selectItem 'terrain'
		EditorEventbus.showTerrainProperties.dispatch()

return SceneExplorer