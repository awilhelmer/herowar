BaseController = require 'controllers/baseController'
db = require 'database'

class EditController extends BaseController

	initialize: (options) ->
		throw "collection should be set" unless @collection
		@options = _.extend {}, options
		@fetchDetails()
		super @options

	fetchDetails: ->
		unless db.get @collection, @options[0]
			col = db.get @collection
			col.fetch id: @options[0] if col

	getEditOptions: ->
		options = {}
		options.modelId = @options[0]
		options

return EditController