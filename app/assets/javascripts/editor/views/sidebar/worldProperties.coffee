EditorEventbus = require 'editorEventbus'
BasePropertiesView = require 'views/basePropertiesView'
templates = require 'templates'

class WorldProperties extends BasePropertiesView
	
	id: 'sidebar-properties-world'
	
	className: 'sidebar-panel'
	
	template: templates.get 'sidebar/worldProperties.tmpl'

	bindEvents: ->
		EditorEventbus.showWorldProperties.add @showPanel
		EditorEventbus.showTerrainProperties.add @hidePanel
		EditorEventbus.showObjectProperties.add @hidePanel
		EditorEventbus.showMaterialProperties.add @hidePanel

return WorldProperties