BaseController = require 'controllers/baseController'
db = require 'database'

class FetchModelController extends BaseController

	initialize: (options) ->
		throw "collection should be set" unless @collection
		@options = _.extend {}, options
		@fetchModel()
		super @options

	fetchModel: ->
		unless db.get @collection, @options[0]
			col = db.get @collection
			col.fetch id: @options[0] if col

return FetchModelController