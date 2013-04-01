AdminAuthView = require 'views/adminAuthView'
templates = require 'templates'
app = require 'application'

###
    The Home shows our home start view.

    @author Sebastian Sachtleben
###
class Home extends AdminAuthView

	id: 'home'
	
	template: templates.get 'home.tmpl'
	
	events:
		'click .users-link': 'users'
		
	users: (event) ->
		event?.preventDefault()
		app.navigate 'admin/user/all', true 
	
return Home