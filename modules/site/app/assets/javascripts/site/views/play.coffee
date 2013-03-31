AuthView = require 'views/authView'
templates = require 'templates'
app = require 'application'

###
    The Play shows our play view.

    @author Sebastian Sachtleben
###
class Play extends AuthView

	id: 'play'
	
	template: templates.get 'play.tmpl'
	
return Play