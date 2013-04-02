EditFormView = require 'views/editFormView'
templates = require 'templates'
app = require 'application'

class MapForm extends EditFormView
	
	entity: 'db/maps'
	
	entityType: 'map'
	
	template: templates.get 'maps/form.tmpl'

return MapForm