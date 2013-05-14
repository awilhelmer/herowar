EditorEventbus = require 'editorEventbus'
ApplicationController = require 'controllers/application'
EditorInput = require 'core/editorInput'
EditorScene = require 'core/editorScene'
EditorTools = require 'core/editorTools'
Variables = require 'variables'
log = require 'util/logger'

class Editor extends ApplicationController

	views:
		'views/iconbar'  				: ''
		'views/scenebar'  			: ''
		'views/sidebarLeft'			: ''
		'views/sidebar'	  			: ''
		'views/viewport'				: ''
		'views/logsystem'				: ''
		'views/modal'						: ''

	viewports: [
		left: 0,
		top: 0,
		width: 1.0,
		height: 1.0,
		background: { r: 0, g: 0, b: 0, a: 1 },
		position: [ 300, 150, 300 ],
		rotation: [ 0, 0, 0 ],
		zoom: 1.0,
		fov: 75,
		domId: '#viewport',
		type: Variables.VIEWPORT_TYPE_EDITOR		
	]

	initialize: (options) ->
		log.info 'Initialize editor...'
		super options
		@bindEvents()
		
	initEngine: ->
		super()
		@engine.pause = true
	
	initCore: ->
		@input = new EditorInput @
		@scene = new EditorScene @
		@tools = new EditorTools @
		
	bindEvents: ->
		EditorEventbus.render.add @render
	
	render: (resize) =>
		if resize then @engine.onWindowResize true else	@engine.render()

return Editor