EditorEventbus = require 'editorEventbus'
BasePropertiesView = require 'views/basePropertiesView'
templates = require 'templates'

class Environment extends BasePropertiesView

	id: 'sidebar-environment'

	className: 'sidebar-panel'

	template: templates.get 'sidebar/environment.tmpl'

	bindEvents: ->
		EditorEventbus.showWorldProperties.add @hidePanel
		EditorEventbus.showTerrainProperties.add @hidePanel
		EditorEventbus.showObjectProperties.add @hidePanel
		EditorEventbus.showMaterialProperties.add @hidePanel
		EditorEventbus.showSidebarEnvironment.add @loadData
		EditorEventbus.showSidebarPathing.add @hidePanel

	initialize: (options) ->
		@loadedEnvironment = false
		super options
	
	loadData: =>
		unless @loadedEnvironment
			@loadedEnvironment = true
			EditorEventbus.treeLoadData.dispatch 'sidebar-environment-categories' 
		@showPanel()

return Environment