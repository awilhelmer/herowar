EditFormView = require 'views/editFormView'
templates = require 'templates'
app = require 'application'

class UserForm extends EditFormView
	
	entity: 'db/users'
	
	entityType: 'user'
	
	template: templates.get 'users/form.tmpl'

return UserForm