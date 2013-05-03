EditorEventbus = require 'editorEventbus'
BasePropertiesView = require 'views/basePropertiesView'
templates = require 'templates'
db = require 'database'

class PathingProperties extends BasePropertiesView
	
	id: 'sidebar-properties-object'
	
	className: 'sidebar-panel hidden'
	
	template: templates.get 'sidebar/pathingProperties.tmpl'

	events:
		'change input[name="name"]'				: 'onChangedString'
		'click #sidebar-waypoint-create'	: 'createWaypoint'

	bindEvents: ->
		EditorEventbus.showWorldProperties.add @hidePanel
		EditorEventbus.showTerrainProperties.add @hidePanel
		EditorEventbus.showObjectProperties.add @hidePanel
		EditorEventbus.showMaterialProperties.add @hidePanel
		EditorEventbus.showSidebarEnvironment.add @hidePanel
		EditorEventbus.showSidebarPathing.add @hidePanel
		EditorEventbus.showPathingProperties.add @showPanel
		EditorEventbus.selectPathUI.add @selectItem

	selectItem: (value) =>
		@model = db.get 'paths', value
		@render()

	createWaypoint: ->
		# TODO: load flag model ...

return PathingProperties