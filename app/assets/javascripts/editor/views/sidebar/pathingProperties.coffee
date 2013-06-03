EditorEventbus = require 'editorEventbus'
BasePropertiesView = require 'views/basePropertiesView'
templates = require 'templates'
Constants = require 'constants'
log = require 'util/logger'
db = require 'database'

class PathingProperties extends BasePropertiesView
	
	id: 'sidebar-properties-object'
	
	className: 'sidebar-panel hidden'
	
	template: templates.get 'sidebar/pathingProperties.tmpl'

	events:
		'change input[name="name"]'				: 'onChangedString'
		'click #sidebar-waypoint-create'	: 'createWaypoint'

	bindEvents: ->
		EditorEventbus.selectPathUI.add @selectItem

	selectItem: (value) =>
		@model = db.get 'db/paths', value
		@render()

	createWaypoint: ->
		log.debug 'Set Tool Waypoint'
		tool = db.get 'ui/tool'
		tool.set 'active', Constants.TOOL_WAYPOINT

return PathingProperties