BaseView = require 'views/baseView'
templates = require 'templates'

###
    The Signup shows the registration form for new users.

    @author Sebastian Sachtleben
###
class Signup extends BaseView

	id: 'signup'
	
	template: templates.get 'signup.tmpl'
	
return Signup