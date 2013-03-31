AuthView = require 'views/authView'
templates = require 'templates'
app = require 'application'

###
    The Home shows our home start view.

    @author Sebastian Sachtleben
###
class Home extends AuthView

	id: 'home'
	
	template: templates.get 'home.tmpl'
	
	redirectTo: 'admin/login'
	
	events:
		'click .users-link': 'users'
		
	users: (event) ->
		event?.preventDefault()
		app.navigate 'admin/users/all', true 
	
return Home