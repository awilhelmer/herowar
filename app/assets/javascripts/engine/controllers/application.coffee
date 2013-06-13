BaseController = require 'controllers/baseController'
Variables = require 'variables'
Scene = require 'core/scene'
events = require 'events'
db = require 'database'

class ApplicationController extends BaseController

	views:
		'views/viewport' : ''

	viewports: [
		domId: '#viewport'
		type: Variables.VIEWPORT_TYPE_RTS
		hud: Variables.HUD_GAME
		camera:
			fov: 40
			type: Variables.CAMERA_TYPE_ORTHOGRAPHIC
			position: [ 0, 800, 350 ]
			rotation: [ THREE.Math.degToRad(-70), 0, 0 ]
	]

	initialize: (options) ->
		super options
		@initViewports()	
		@initEngine()
		@initCore()
		events.trigger 'created:controller:application'
		return
		
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

return ApplicationController