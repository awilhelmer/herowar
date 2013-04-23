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

	bindEvents: ->
		EditorEventbus.selectWorldViewport.add @selectWorld
		EditorEventbus.selectTerrainViewport.add @selectTerrain
		EditorEventbus.selectObjectViewport.add @selectObject

	render: ->
		super()
		# TODO: handle this with a model
		$scenegraphTree = @$el.find '.scenegraph-tree'
		$scenegraphTree.append '<div class="scenegraph-tree-world active" data-type="world"><img src="assets/images/editor/world.png" /><span>World</span></div>'
		$scenegraphTree.append '<div class="scenegraph-tree-terrain" data-type="terrain"><img src="assets/images/editor/terrain.jpg" /><span>Terrain</span></div>'
		$scenegraphTree.append '<div class="scenegraph-tree-folder scenegraph-tree-environment" data-type="environment"><img src="assets/images/editor/folder.png" /><span>Environment</span></div>'
		$scenegraphTree.append '<div class="scenegraph-tree-folder scenegraph-tree-pathing" data-type="pathing"><img src="assets/images/editor/folder.png" /><span>Pathing</span></div>'

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
			when 'object'
				EditorEventbus.selectObjectUI.dispatch()
				@selectObject()
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

	selectObject: =>
		@selectItem 'object'
		EditorEventbus.showObjectProperties.dispatch()

return SceneExplorer