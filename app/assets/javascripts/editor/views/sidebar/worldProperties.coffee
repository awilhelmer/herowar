EditorEventbus = require 'editorEventbus'
BasePropertiesView = require 'views/basePropertiesView'
templates = require 'templates'

class WorldProperties extends BasePropertiesView

	id: 'sidebar-properties-world'

	className: 'sidebar-panel'

	entity: 'world'

	template: templates.get 'sidebar/worldProperties.tmpl'

	initialize: (options) ->
		super options
		console.log @model

	bindEvents: ->
		EditorEventbus.showWorldProperties.add @showPanel
		EditorEventbus.showTerrainProperties.add @hidePanel
		EditorEventbus.showObjectProperties.add @hidePanel
		EditorEventbus.showMaterialProperties.add @hidePanel

return WorldProperties