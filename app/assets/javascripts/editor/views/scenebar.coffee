EditorEventbus = require 'editorEventbus'
BaseView = require 'views/baseView'
templates = require 'templates'

class ScenebarView extends BaseView

	id: 'scenebar'
	
	template: templates.get 'scenebar.tmpl'

	events:
		'shown a[data-toggle="tab"]' : 'toggleTab'

	initialize: (options) ->
		#@loadedEnvironments = false
		super options

	toggleTab: (event) =>
		# get it of tab container
		id = $(event.target).attr('href').split('#')[1]
		EditorEventbus.toggleTab.dispatch id
		#unless @loadedEnvironments
		#	console.log 'LOAD EVIRONMENTS ...'
		#	@loadedEnvironments = true
		#	EditorEventbus.treeLoadData.dispatch 'sidebar-environment-categories' 		

return ScenebarView