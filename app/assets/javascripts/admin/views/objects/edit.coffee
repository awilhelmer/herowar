AdminAuthView = require 'views/adminAuthView'
templates = require 'templates'
db = require 'database'

class ObjectEdit extends AdminAuthView

	entity: 'db/objects'
	
	template: templates.get 'objects/edit.tmpl'
	
	bindEvents: ->
		unless @model
			collection = db.get @entity
			@listenTo collection, 'add remove change reset', @render if collection
		super()

	render: ->
		@model = db.get @entity, @options.modelId unless @model
		super() if @model

return ObjectEdit