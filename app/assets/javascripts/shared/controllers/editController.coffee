BaseController = require 'controllers/baseController'

class EditController extends BaseController

	initialize: (options) ->
		@options = _.extend {}, options
		super @options

	getEditOptions: ->
		options = {}
		options.modelId = @options[0]
		options

return EditController