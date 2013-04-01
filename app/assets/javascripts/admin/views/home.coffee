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
		'click .user-link'	: 'user'
		'click .map-link'		: 'map'
		
	user: (event) ->
		event?.preventDefault()
		app.navigate 'admin/user/all', true
		
	map: (event) ->
		event?.preventDefault()
		app.navigate 'admin/map/all', true
	
return Home