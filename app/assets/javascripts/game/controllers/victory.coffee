BaseController = require 'controllers/baseController'
engine = require 'engine'
log = require 'util/logger'

class VictoryController extends BaseController

	views:
		'views/stats'	    : ''
		'views/victory'		: ''
		'views/viewport'	: ''

	initialize: (options) ->
		log.info 'Initialize victory...'
		super options
		engine.stop()
		
return VictoryController