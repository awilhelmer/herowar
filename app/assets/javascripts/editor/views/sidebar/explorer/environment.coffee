EditorEventbus = require 'editorEventbus'
BaseView = require 'views/baseView'
templates = require 'templates'

class EnvironmentExplorer extends BaseView

	entity: 'environmentsStatic'

	template: templates.get 'sidebar/explorer/environment.tmpl'

	events:
		'click .scenegraph-tree-object' : 'selectElement'

	bindEvents: ->
		@listenTo @model, 'add remove change reset', @render if @model

	selectElement: (event) ->
		unless event then return
		event.preventDefault()
		$currentTarget = $ event.currentTarget
		value = $currentTarget.data 'value'
		$('.scenegraph-tree div').removeClass 'active'
		$currentTarget.addClass 'active'
		EditorEventbus.selectObjectUI.dispatch value
		EditorEventbus.showObjectProperties.dispatch()
		
return EnvironmentExplorer