AdminAuthView = require 'views/adminAuthView'
templates = require 'templates'

class MapsEdit extends AdminAuthView

	entity: 'db/maps'
	
	template: templates.get 'maps/edit.tmpl'

return MapsEdit