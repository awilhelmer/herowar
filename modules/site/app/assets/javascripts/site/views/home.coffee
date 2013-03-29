BaseView = require 'views/baseView'
templates = require 'templates'

###
    The Home shows our home start view.

    @author Sebastian Sachtleben
###
class Home extends BaseView

	id: 'home'
	
	template: templates.get 'home.tmpl'
	
return Home