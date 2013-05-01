EditorEventbus = require 'editorEventbus'
BaseController = require 'controllers/baseController'
Input = require 'core/input'
Scene = require 'core/scene'
Tools = require 'core/tools'
log = require 'util/logger'
db = require 'database'
Engine = require 'engine'
Variables = require 'variables'

class Editor extends BaseController

	views:
		'views/iconbar'  				: ''
		'views/scenebar'  			: ''
		'views/sidebarLeft'			: ''
		'views/sidebar'	  			: ''
		'views/viewport'				: ''
		'views/logsystem'				: ''
		'views/modal'						: ''

	initialize: (options) ->
		log.info 'Initialize editor...'
		super options
		@data = db.data()
		@initEngine()
		@bindEvents()
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
		@input = new Input @
		@scene = new Scene @
		@tools = new Tools @

	bindEvents: ->
		EditorEventbus.render.add @render
	
	render: (resize) =>
		if resize then @engine.onWindowResize true else	@engine.render()

return Editor