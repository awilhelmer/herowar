BaseView = require 'views/baseView'
templates = require 'templates'

###
    The Home shows our home start view.

    @author Sebastian Sachtleben
###
class Login extends BaseView

	id: 'login'
	
	template: templates.get 'home/login.tmpl'
	
return Login