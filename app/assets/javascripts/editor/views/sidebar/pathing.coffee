EditorEventbus = require 'editorEventbus'
BasePropertiesView = require 'views/basePropertiesView'
templates = require 'templates'

class Pathing extends BasePropertiesView

	id: 'sidebar-pathing'

	className: 'sidebar-panel'

	template: templates.get 'sidebar/pathing.tmpl'

	bindEvents: ->
		EditorEventbus.showWorldProperties.add @hidePanel
		EditorEventbus.showTerrainProperties.add @hidePanel
		EditorEventbus.showObjectProperties.add @hidePanel
		EditorEventbus.showMaterialProperties.add @hidePanel
		EditorEventbus.showSidebarEnvironment.add @hidePanel
		EditorEventbus.showSidebarPathing.add @showPanel

return Pathing