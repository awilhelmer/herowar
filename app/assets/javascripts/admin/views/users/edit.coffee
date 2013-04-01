AdminAuthView = require 'views/adminAuthView'
templates = require 'templates'
app = require 'application'

class UsersEdit extends AdminAuthView

	entity: 'db/users'
	
	template: templates.get 'users/edit.tmpl'
	
	events:
		'click .save-button'		: 'saveEntry'
		'click .cancel-button'	: 'cancel'
	
	initialize: (options) ->
		super options
		console.log @model
		
	saveEntry: (event) ->
		event?.preventDefault()
		console.log 'Save user here'
		
	cancel: (event) ->
		event?.preventDefault()
		app.navigate 'admin/user/all', true 		

return UsersEdit