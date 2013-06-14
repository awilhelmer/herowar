ApplicationController = require 'controllers/application'
#PathingHelper = require 'helper/pathingHelper'
GameScene = require 'core/gameScene'
GameTools = require 'core/gameTools'
engine = require 'engine'
log = require 'util/logger'

class GameController extends ApplicationController

	views:
		'views/build'       : ''
		'views/chat'        : ''
		'views/stats'       : ''
		'views/viewport'    : ''
		'views/debug'       : ''
		'views/menubar'     : ''

	initialize: (options) ->
		log.info 'Initialize game...'
		super options
		# Show path during develop - Should be removed later
		#@pathingHelper = new PathingHelper()
		#@pathingHelper.showPath()
		engine.start()

	initCore: ->
		@scene = new GameScene()
		@tools = new GameTools()
		
return GameController