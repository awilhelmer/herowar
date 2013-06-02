BaseController = require 'controllers/baseController'
engine = require 'engine'
log = require 'util/logger'

class DefeatController extends BaseController

	views:
		'views/defeat'		: ''
		'views/stats'	    : ''
		'views/viewport'	: ''

	initialize: (options) ->
		log.info 'Initialize defeat...'
		super options
		engine.stop()
		
return DefeatController