EditorEventbus = require 'editorEventbus'
BaseView = require 'views/baseView'
templates = require 'templates'

class ScenebarEnvironmentsView extends BaseView
	
	template: templates.get 'scenebar/environments.tmpl'

	bindEvents: ->
		EditorEventbus.toggleTab.add @toggleTab
	
	initialize: (options) ->
		@loadedEnvironments = false
		super options
	
	toggleTab: (id) =>
		if id is 'scenebar-environments' and !@loadedEnvironments
			console.log 'Load Environments in Scenebar ...'
			@loadedEnvironments = true
			EditorEventbus.treeLoadData.dispatch 'sidebar-environment-categories' 	

return ScenebarEnvironmentsView