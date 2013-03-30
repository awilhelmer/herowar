BaseView = require 'views/baseView'
templates = require 'templates'
app = require 'application'

###
    The Home shows our home start view.

    @author Sebastian Sachtleben
###
class Home extends BaseView

	id: 'home'
	
	template: templates.get 'home.tmpl'
	
	events:
		'click .signup-link': 'signup'
		
	signup: (event) ->
		event?.preventDefault()
		app.navigate 'signup', true 
	
return Home