ApplicationController = require 'controllers/application'
log = require 'util/logger'

class GameController extends ApplicationController

	views:
		'views/progress'	: ''
		'views/build'			: ''
		'views/viewport'	: ''

	initialize: (options) ->
		log.info 'Initialize game...'
		super options
		@engine.start()

return GameController