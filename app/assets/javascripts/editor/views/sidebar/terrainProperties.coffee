EditorEventbus = require 'editorEventbus'
BaseView = require 'views/baseView'
templates = require 'templates'

class TerrainProperties extends BaseView
	
	id: 'sidebar-properties-terrain'
	
	className: 'sidebar-panel hidden'
	
	template: templates.get 'sidebar/terrainProperties.tmpl'

	bindEvents: ->
		EditorEventbus.showWorldProperties.add @hidePanel
		EditorEventbus.showTerrainProperties.add @showPanel
		EditorEventbus.showObjectProperties.add @hidePanel

	hidePanel: =>
		@$el.addClass 'hidden'

	showPanel: =>
		@$el.removeClass 'hidden'

return TerrainProperties