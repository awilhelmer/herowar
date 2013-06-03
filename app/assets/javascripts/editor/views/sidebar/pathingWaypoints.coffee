EditorEventbus = require 'editorEventbus'
BaseView = require 'views/baseView'
templates = require 'templates'

class PathingWaypoints extends BaseView

	entity: 'db/waypoints'
		
	template: templates.get 'util/list.tmpl'

	initialize: (options) ->
		@currentPathId = -1
		super options

	bindEvents: ->
		@listenTo @model, 'add remove change reset', @render
		EditorEventbus.selectPathUI.add @selectPathId

	getTemplateData: ->
		json = []
		json.push value.toJSON() for value in @model.where path: @currentPathId
		json

	selectPathId: (value) =>
		@currentPathId = value
		@render()

return PathingWaypoints