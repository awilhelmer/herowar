EditController = require 'controllers/editController'

class MapsEditController extends EditController

	views:
		'views/header'			: ''
		'views/maps/edit' 	: 'getEditOptions'
	
	collection: 'db/maps'

return MapsEditController