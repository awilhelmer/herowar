ApplicationController = require 'controllers/application'
log = require 'util/logger'

class GameController extends ApplicationController

	views:
		'views/progress'	: ''
		'views/viewport'	: ''

	initialize: (options) ->
		log.info 'Initialize game...'
		super options

return GameController