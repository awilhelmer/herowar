EditController = require 'controllers/editController'

class NewsEditController extends EditController

	views:
		'views/header'				: ''
		'views/news/edit'		 	: 'getEditOptions'

	collection: 'db/newss'

return NewsEditController