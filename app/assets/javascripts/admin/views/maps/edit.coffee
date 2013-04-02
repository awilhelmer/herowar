AdminAuthView = require 'views/adminAuthView'
templates = require 'templates'
db = require 'database'

class MapEdit extends AdminAuthView

	entity: 'db/maps'
	
	template: templates.get 'maps/edit.tmpl'

	bindEvents: ->
		unless @model
			collection = db.get @entity
			@listenTo collection, 'add remove change reset', @render if collection
		super()

	render: ->
		@model = db.get @entity, @options.modelId unless @model
		super() if @model

return MapEdit