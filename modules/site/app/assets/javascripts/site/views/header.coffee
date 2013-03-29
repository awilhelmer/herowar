BaseView = require 'views/baseView'
templates = require 'templates'

###
    The Header shows our top menu.

    @author Sebastian Sachtleben
###
class Header extends BaseView

	id: 'header'
	
	template: templates.get 'header.tmpl'
	
return Header