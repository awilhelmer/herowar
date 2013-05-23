BaseController = require 'controllers/baseController'
Variables = require 'variables'
Scene = require 'core/scene'
db = require 'database'

class ApplicationController extends BaseController

	views:
		'views/viewport' : ''

	viewports: [
		domId: '#viewport',
		type: Variables.VIEWPORT_TYPE_RTS,
		camera:
			position: [ 0, 350, 0 ],
			rotation: [ -Math.PI/2, 0, 0 ]
	]

	initialize: (options) ->
		super options
		@initViewports()	
		@initEngine()
		@initCore()
	
	initViewports: ->
		for viewport in @viewports
			@initViewport viewport	
	
	initViewport: (viewport) ->
		viewports = db.get 'ui/viewports'
		viewports.add viewport
	
	initEngine: ->
		engine = require 'engine'
		engine.initialize()
		engine
	
	initCore: ->
		@scene = new Scene()

return ApplicationController