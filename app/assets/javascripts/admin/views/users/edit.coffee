AdminAuthView = require 'views/adminAuthView'
templates = require 'templates'
app = require 'application'

class UsersEdit extends AdminAuthView

	entity: 'db/users'
	
	template: templates.get 'users/edit.tmpl'	

return UsersEdit