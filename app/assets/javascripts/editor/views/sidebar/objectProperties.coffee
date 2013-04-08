EditorEventbus = require 'editorEventbus'
BasePropertiesView = require 'views/basePropertiesView'
templates = require 'templates'

class ObjectProperties extends BasePropertiesView
	
	id: 'sidebar-properties-object'
	
	className: 'sidebar-panel hidden'
	
	template: templates.get 'sidebar/objectProperties.tmpl'
	
	bindEvents: ->
		EditorEventbus.showWorldProperties.add @hidePanel
		EditorEventbus.showTerrainProperties.add @hidePanel
		EditorEventbus.showObjectProperties.add @showPanel

return ObjectProperties