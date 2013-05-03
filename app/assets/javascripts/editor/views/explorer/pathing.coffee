EditorEventbus = require 'editorEventbus'
BaseView = require 'views/baseView'
templates = require 'templates'

class PathingExplorer extends BaseView

	entity: 'paths'

	template: templates.get 'explorer/item.tmpl'

	events:
		'click .scenegraph-tree-object' : 'selectElement'

	bindEvents: ->
		@listenTo @model, 'add remove change reset', @render if @model

	selectElement: (event) ->
		unless event then return
		event.preventDefault()

return PathingExplorer