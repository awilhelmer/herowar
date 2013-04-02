AdminAuthView = require 'views/adminAuthView'
templates = require 'templates'

class MapNew extends AdminAuthView

	id: 'map-new'
	
	template: templates.get 'maps/new.tmpl'

return MapNew