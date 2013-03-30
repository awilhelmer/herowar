BaseView = require 'views/baseView'
templates = require 'templates'
app = require 'application'

###
    The Header shows our top menu.

    @author Sebastian Sachtleben
###
class Header extends BaseView

	id: 'header'
	
	template: templates.get 'header.tmpl'
	
	events:
		'click .home-link': 'home'
		
	home: (event) ->
		event?.preventDefault()
		app.navigate '', true 
	
return Header