AuthView = require 'views/authView'
templates = require 'templates'
app = require 'application'

###
    The UsersAll shows a list of users

    @author Sebastian Sachtleben
###
class UsersAll extends AuthView

	entity: 'api/users'

	id: 'usersAll'
	
	template: templates.get 'usersAll.tmpl'
	
	redirectTo: 'admin/login'
	
	initialize: (options) ->
		super options
		@model.fetch()
		
	bindEvents: ->
		@listenTo @model, 'add remove change reset', @render if @model

	render: ->
		console.log 'Render user all view'
		console.log @model
		super()

return UsersAll