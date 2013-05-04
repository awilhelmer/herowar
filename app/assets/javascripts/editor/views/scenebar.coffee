EditorEventbus = require 'editorEventbus'
BaseView = require 'views/baseView'
templates = require 'templates'

class ScenebarView extends BaseView

	id: 'scenebar'
	
	template: templates.get 'scenebar.tmpl'

	events:
		'shown a[data-toggle="tab"]' : 'toggleTab'

	toggleTab: (event) =>
		id = $(event.target).attr('href').split('#')[1]
		EditorEventbus.dispatch 'toggleTab',id	

return ScenebarView