BaseController = require 'controllers/baseController'
Variables = require 'variables'
Scene = require 'core/scene'
Engine = require 'engine'
db = require 'database'

class ApplicationController extends BaseController

	views:
		'views/viewport'				: ''

	initialize: (options) ->
		super options
		@data = db.data()
		@initEngine()
		@initCore()
		
	initEngine: ->
		@engine = new Engine
			container : $ '#viewport'
			data : @data
			views :  [
				left: 0,
				bottom: 0,
				width: 1.0,
				height: 1.0,
				background: { r: 0, g: 0, b: 0, a: 1 },
				eye: [ 300, 150, 300 ],
				up: [ 1, 1, 1 ],
				fov: 75,
				type: Variables.CAMERA_TYPE_FREE		
			]
		@engine.init()
		@engine.pause = true
	
	initCore: ->
		@scene = new Scene @

	
	render: (resize) =>
		if resize then @engine.onWindowResize true else	@engine.render()

return ApplicationController