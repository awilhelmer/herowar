ApplicationController = require 'controllers/application'
PathingHelper = require 'helper/pathingHelper'
GameTools = require 'core/gameTools'
log = require 'util/logger'

class GameController extends ApplicationController

	views:
		'views/progress'	: ''
		'views/build'			: ''
		'views/viewport'	: ''

	initialize: (options) ->
		log.info 'Initialize game...'
		super options
		# Show path during develop - Should be removed later
		@pathingHelper = new PathingHelper @
		@pathingHelper.showPath()
		@engine.start()

	initCore: ->
		@tools = new GameTools @
		super()

return GameController