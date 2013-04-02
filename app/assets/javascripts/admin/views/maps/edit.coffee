AdminAuthView = require 'views/adminAuthView'
templates = require 'templates'

class MapEdit extends AdminAuthView

	entity: 'db/maps'
	
	template: templates.get 'maps/edit.tmpl'

return MapEdit