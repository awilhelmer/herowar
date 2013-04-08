EditorEventbus = require 'editorEventbus'
BasePropertiesView = require 'views/basePropertiesView'
templates = require 'templates'

class MaterialProperties extends BasePropertiesView
	
	id: 'sidebar-properties-material'
	
	className: 'sidebar-panel hidden'
	
	template: templates.get 'sidebar/materialProperties.tmpl'
	
	bindEvents: ->
		EditorEventbus.showWorldProperties.add @hidePanel
		EditorEventbus.showTerrainProperties.add @hidePanel
		EditorEventbus.showObjectProperties.add @hidePanel
		EditorEventbus.showMaterialProperties.add @showPanel

return MaterialProperties