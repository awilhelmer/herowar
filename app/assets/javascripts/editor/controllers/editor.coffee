BaseController = require 'controllers/baseController'
Input = require 'core/input'
Scene = require 'core/scene'
Tools = require 'core/tools'
db = require 'database'
Engine = require 'engine'
Variables = require 'variables'

class Editor extends BaseController

	views:
		'views/menubar'	  : ''
		'views/iconbar'	  : ''
		'views/sidebar'	  : ''
		'views/viewport'	: ''

	initialize: (options) ->
		console.log 'Initialize editor...'
		super options
		@initEngine()
		@initCore()
		
	initEngine: ->
		@engine = new Engine
			container : $ '#viewport'
			data : db.data()
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
	
	initCore: ->
		@input = new Input @
		@scene = new Scene @
		@tools = new Tools @

return Editor