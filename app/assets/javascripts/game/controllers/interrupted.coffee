BaseController = require 'controllers/baseController'
engine = require 'engine'
log = require 'util/logger'

class InterruptedController extends BaseController

	views:
		'views/interrupted' : ''
		'views/viewport'	  : ''

	initialize: (options) ->
		log.info 'Initialize interrupted...'
		super options
		engine.stop()
		
return InterruptedController