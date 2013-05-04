EditorEventbus = require 'editorEventbus'
BaseView = require 'views/baseView'
templates = require 'templates'
db = require 'database'

class PathingExplorer extends BaseView

	entity: 'paths'

	template: templates.get 'explorer/item.tmpl'

	events:
		'click .scenegraph-tree-object' : 'selectElement'

	initialize: (options) ->
		@sidebar = db.get 'ui/sidebar'
		super options

	bindEvents: ->
		@listenTo @model, 'add remove change reset', @render if @model

	selectElement: (event) ->
		unless event then return
		event.preventDefault()
		$currentTarget = $ event.currentTarget
		value = $currentTarget.data 'value'
		$('.scenegraph-tree div').removeClass 'active'
		$currentTarget.addClass 'active'
		EditorEventbus.dispatch 'selectPathUI', value
		@sidebar.set 'active', 'sidebar-properties-pathing'

return PathingExplorer