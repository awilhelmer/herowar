EditorEventbus = require 'editorEventbus'
BaseView = require 'views/baseView'
templates = require 'templates'
log = require 'util/logger'

class ScenebarEnvironmentsView extends BaseView
	
	template: templates.get 'scenebar/environments.tmpl'

	events:
		'mouseover #sidebar-environment-categories-list' 	: 'increasePanelHeight'
		'mouseout #sidebar-environment-categories-list' 	: 'decreasePanelHeight'
		'mouseover #sidebar-environment-geometries-list' 	: 'increasePanelHeight'
		'mouseout #sidebar-environment-geometries-list' 	: 'decreasePanelHeight'

	bindEvents: ->
		EditorEventbus.toggleTab.add @toggleTab
	
	initialize: (options) ->
		@loadedEnvironments = false
		super options
	
	increasePanelHeight: (event) ->
		unless event then return
		$currentTarget = $ event.currentTarget
		$currentTarget.stop().animate height: 300, 300

	decreasePanelHeight: (event) ->
		unless event then return
		$currentTarget = $ event.currentTarget
		$currentTarget.stop().animate height: 33, 300
	
	toggleTab: (id) =>
		if id is 'scenebar-environments' and !@loadedEnvironments
			log.debug 'Load Environments in Scenebar ...'
			@loadedEnvironments = true
			EditorEventbus.dispatch 'treeLoadData', 'sidebar-environment-categories-list' 	

return ScenebarEnvironmentsView