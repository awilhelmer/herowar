AdminAuthView = require 'views/adminAuthView'
templates = require 'templates'

###
    The MapsList shows a list of maps.

    @author Sebastian Sachtleben
###
class MapsList extends AdminAuthView

	entity: 'api/maps'

	id: 'maps-list'
	
	template: templates.get 'maps/list.tmpl'

return MapsList