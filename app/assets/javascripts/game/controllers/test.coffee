BaseController = require 'controllers/baseController'
log = require 'util/logger'

class TestController extends BaseController

	views:
		'views/menubar'     : ''
		
	initialize: (options) ->
		log.info 'Initialize test...'
		super options
		
return TestController