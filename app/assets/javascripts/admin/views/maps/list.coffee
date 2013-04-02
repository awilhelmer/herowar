AdminAuthView = require 'views/adminAuthView'
templates = require 'templates'

class MapList extends AdminAuthView

	entity: 'api/maps'

	id: 'map-list'
	
	template: templates.get 'maps/list.tmpl'

return MapList