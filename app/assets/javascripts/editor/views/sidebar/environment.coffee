EditorEventbus = require 'editorEventbus'
BasePropertiesView = require 'views/basePropertiesView'
templates = require 'templates'

class Environment extends BasePropertiesView

	id: 'sidebar-environment'

	className: 'sidebar-panel'
		
	entity: 'environment'

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
			@model.fetch
				success	: @onSuccess
		@showPanel()
	
	onSuccess: (data) =>
		console.log data
		@render()
	
	render: ->
		console.log 'Render Environment...'
		console.log @getTemplateData()
		super()

return Environment