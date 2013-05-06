FetchModelController = require 'controllers/fetchModelController'
db = require 'database'

class EditController extends FetchModelController

	getEditOptions: ->
		options = {}
		options.modelId = @options[0]
		options

return EditController