FetchCollectionController = require 'controllers/fetchCollectionController'

class PlayController extends FetchCollectionController

	views:
		'views/header'	: ''
		'views/play'		: ''

	collection: 'api/maps'

return PlayController