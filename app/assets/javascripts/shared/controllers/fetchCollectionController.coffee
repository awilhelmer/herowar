BaseController = require 'controllers/baseController'
db = require 'database'

class FetchCollectionController extends BaseController

	initialize: (options) ->
		throw "collection should be set" unless @collection
		@options = _.extend {}, options
		@fetchCollection()
		super @options

	fetchCollection: ->
		col = db.get @collection
		col.fetch() if col

return FetchCollectionController