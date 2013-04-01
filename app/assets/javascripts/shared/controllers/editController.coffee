BaseController = require 'controllers/baseController'

class EditController extends BaseController

	initialize: (options) ->
		@options = _.extend {}, options
		console.log @options
		super @options

	getEditOptions: ->
		options = {}
		options.modelId = @options[0]
		console.log options
		options

return EditController