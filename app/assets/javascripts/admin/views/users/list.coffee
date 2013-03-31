AdminAuthView = require 'views/adminAuthView'
templates = require 'templates'
app = require 'application'

###
    The UsersList shows a list of users

    @author Sebastian Sachtleben
###
class UsersList extends AdminAuthView

	entity: 'api/users'

	id: 'usersAll'
	
	template: templates.get 'users/list.tmpl'
	
	initialize: (options) ->
		super options
		@model.fetch()
		
	bindEvents: ->
		@listenTo @model, 'add remove change reset', @render if @model

return UsersList