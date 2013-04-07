EditorEventbus = require 'editorEventbus'
BaseView = require 'views/baseView'
templates = require 'templates'

class ObjectProperties extends BaseView
	
	id: 'sidebar-properties-object'
	
	className: 'sidebar-panel hidden'
	
	template: templates.get 'sidebar/objectProperties.tmpl'
	
	bindEvents: ->
		EditorEventbus.showWorldProperties.add @hidePanel
		EditorEventbus.showTerrainProperties.add @hidePanel
		EditorEventbus.showObjectProperties.add @showPanel

	hidePanel: =>
		@$el.addClass 'hidden'

	showPanel: =>
		@$el.removeClass 'hidden'

return ObjectProperties