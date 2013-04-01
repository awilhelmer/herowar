AdminAuthView = require 'views/adminAuthView'
templates = require 'templates'
app = require 'application'

###
    The MapNew allows to create a new map.

    @author Sebastian Sachtleben
###
class MapNew extends AdminAuthView

	id: 'map-new'
	
	template: templates.get 'maps/new.tmpl'
	
	events:
		'click .save-button'		: 'saveEntry'
		'click .cancel-button'	: 'cancel'
			
	saveEntry: (event) ->
		event?.preventDefault()
		console.log 'Save entry here'
		
	cancel: (event) ->
		event?.preventDefault()
		app.navigate 'admin/map/all', true 		

return MapNew