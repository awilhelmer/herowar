EditorEventbus = require 'editorEventbus'
BaseView = require 'views/baseView'
templates = require 'templates'

class WorldProperties extends BaseView
	
	id: 'sidebar-properties-world'
	
	className: 'sidebar-panel'
	
	template: templates.get 'sidebar/worldProperties.tmpl'

	bindEvents: ->
		EditorEventbus.showWorldProperties.add @showPanel
		EditorEventbus.showTerrainProperties.add @hidePanel
		EditorEventbus.showObjectProperties.add @hidePanel

	hidePanel: =>
		@$el.addClass 'hidden'

	showPanel: =>
		@$el.removeClass 'hidden'

return WorldProperties