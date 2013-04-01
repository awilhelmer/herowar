AdminAuthView = require 'views/adminAuthView'
templates = require 'templates'
app = require 'application'

###
    The UsersList shows a list of users

    @author Sebastian Sachtleben
###
class UserShow extends AdminAuthView

	entity: 'db/users'

	id: 'user-show'
	
	template: templates.get 'users/show.tmpl'
	
	events:
		'click .save-button'		: 'saveUser'
		'click .cancel-button'	: 'cancel'
	
	initialize: (options) ->
		super options
		console.log @model
		
	saveUser: (event) ->
		event?.preventDefault()
		console.log 'Save user here'
		
	cancel: (event) ->
		event?.preventDefault()
		app.navigate 'admin/user/all', true 		

return UserShow