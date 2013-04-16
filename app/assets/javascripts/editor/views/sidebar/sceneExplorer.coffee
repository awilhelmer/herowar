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
		'keyup #inputTitle'						: 'changeTitle'
		'change #inputTitle'					: 'changeTitle'
		'keyup #inputDescription'			: 'changeDescription'
		'change #inputDescription'		: 'changeDescription'
		'submit form'									: 'ignoreSubmitForm'

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
		$scenegraphTree = @$el.find '.scenegraph-tree'
		$scenegraphTree.append '<div class="scenegraph-tree-world active" data-type="world"><img src="assets/images/editor/world.png" /><span>World</span></div>'
		$scenegraphTree.append '<div class="scenegraph-tree-terrain" data-type="terrain"><img src="assets/images/editor/terrain.jpg" /><span>Terrain</span></div>'
		$scenegraphTree.append '<div class="scenegraph-tree-folder" data-type="folder"><img src="assets/images/editor/folder.png" /><span>Environment</span></div>'
		$scenegraphTree.append '<div class="scenegraph-tree-folder" data-type="folder"><img src="assets/images/editor/folder.png" /><span>Pathing</span></div>'
	
	worldAdded: =>
		@world = true

	terrainAdded: =>
		@terrain = true
	
	selectElement: (event) ->
		$currentTarget = $ event.currentTarget
		type = $currentTarget.data 'type'
		# TODO: this is not cool :/
		if type is 'world'
			EditorEventbus.selectWorldUI.dispatch()
			@selectWorld()
		else if type is 'terrain'
			EditorEventbus.selectTerrainUI.dispatch()
			@selectTerrain()
		else if type is 'object'
			EditorEventbus.selectObjectUI.dispatch()
			@selectObject()
		else
			console.log "ERROR type \"#{type}\" is unknowned"

	selectWorld: =>
		@$el.find('.scenegraph-tree div').removeClass 'active'
		@$el.find('.scenegraph-tree-world').addClass 'active'
		EditorEventbus.showWorldProperties.dispatch()

	selectTerrain: =>
		@$el.find('.scenegraph-tree div').removeClass 'active'
		@$el.find('.scenegraph-tree-terrain').addClass 'active'
		EditorEventbus.showTerrainProperties.dispatch()

	selectObject: =>
		@$el.find('.scenegraph-tree div').removeClass 'active'
		@$el.find('.scenegraph-tree-object').addClass 'active'
		EditorEventbus.showObjectProperties.dispatch()

	changeTitle: (event) =>
		unless event then return
		$currentTarget = $ event.currentTarget
		MapProperties.MAP_TITLE = $currentTarget.val()

	changeDescription: (event) =>
		unless event then return
		$currentTarget = $ event.currentTarget
		MapProperties.MAP_DESCRIPTION = $currentTarget.val()
	
	ignoreSubmitForm: (event) =>
		unless event then return
		event.preventDefault()

return SceneExplorer