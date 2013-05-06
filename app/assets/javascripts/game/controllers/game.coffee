ApplicationController = require 'controllers/application'
log = require 'util/logger'

class Game extends ApplicationController

	initialize: (options) ->
		log.info 'Initialize game...'
		super options

return Editor