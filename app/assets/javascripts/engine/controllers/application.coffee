BaseController = require 'controllers/baseController'
SocketClient = require 'network/socketclient'
Variables = require 'variables'
Scene = require 'core/scene'
Engine = require 'engine'
db = require 'database'

class ApplicationController extends BaseController

	views:
		'views/viewport'				: ''

	viewports: [
		left: 0,
		bottom: 0,
		width: 1.0,
		height: 1.0,
		background: { r: 0, g: 0, b: 0, a: 1 },
		eye: [ 0, 200, 0 ],
		up: [ 0, 0, 1 ],
		fov: 75,
		type: Variables.CAMERA_TYPE_RTS
	]

	initialize: (options) ->
		super options
		@data = db.data()
		@initEngine()
		@initCore()
		
	initEngine: ->
		@engine = new Engine
			container : $ '#viewport'
			data : @data
			views : @viewports
		@engine.init()
	
	initCore: ->
		@scene = new Scene @
		@socketClient = new SocketClient @
	
	render: (resize) =>
		if resize then @engine.onWindowResize true else	@engine.render()

return ApplicationController