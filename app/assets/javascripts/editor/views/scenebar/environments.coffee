EditorEventbus = require 'editorEventbus'
BaseView = require 'views/baseView'
templates = require 'templates'
log = require 'util/logger'

class ScenebarEnvironmentsView extends BaseView
	
	template: templates.get 'scenebar/environments.tmpl'

	bindEvents: ->
		EditorEventbus.toggleTab.add @toggleTab
	
	initialize: (options) ->
		@loadedEnvironments = false
		super options
	
	toggleTab: (id) =>
		if id is 'scenebar-environments' and !@loadedEnvironments
			log.debug 'Load Environments in Scenebar ...'
			@loadedEnvironments = true
			EditorEventbus.dispatch 'treeLoadData', 'sidebar-environment-categories-list' 	

return ScenebarEnvironmentsView