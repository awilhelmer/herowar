EditController = require 'controllers/editController'

class ObjectEditController extends EditController

	views:
		'views/header'				: ''
		'views/objects/edit' 	: 'getEditOptions'

return ObjectEditController