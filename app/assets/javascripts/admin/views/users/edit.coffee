AdminAuthView = require 'views/adminAuthView'
templates = require 'templates'
db = require 'database'

class UsersEdit extends AdminAuthView

	entity: 'db/users'
	
	template: templates.get 'users/edit.tmpl'	

	bindEvents: ->
		unless @model
			collection = db.get @entity
			@listenTo collection, 'add remove change reset', @render if collection
		super()

	render: ->
		@model = db.get @entity, @options.modelId unless @model
		super() if @model

return UsersEdit