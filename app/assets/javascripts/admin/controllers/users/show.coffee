BaseController = require 'controllers/baseController'

###
    The users show controller shows a a single user.

    @author Sebastian Sachtleben
###
class UsersShowController extends BaseController

	views:
		'views/header'			: ''
		'views/users/show' 	: 'getShowOptions'

	initialize: (options) ->
		@options = _.extend {}, options
		console.log @options
		super @options

	getShowOptions: ->
		options = {}
		options.modelId = @options[0]
		console.log options
		options

return UsersShowController