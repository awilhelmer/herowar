AuthView = require 'views/authView'
templates = require 'templates'
app = require 'application'

###
    The UsersAll shows a list of users

    @author Sebastian Sachtleben
###
class UsersAll extends AuthView

	id: 'usersAll'
	
	template: templates.get 'usersAll.tmpl'
	
	redirectTo: 'admin/login'

return UsersAll