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
		domId: '#viewport',
		type: Variables.VIEWPORT_TYPE_EDITOR
		camera:
			type: Variables.CAMERA_TYPE_PERSPECTIVE
			position: [ 0, 300, 300 ]
			rotation: [ THREE.Math.degToRad(-70), 0, 0 ]
	]

	initialize: (options) ->
		log.info 'Initialize editor...'
		super options
		
	initEngine: ->
		engine = super()
		engine.pause = true
	
	initCore: ->
		@input = new EditorInput()
		@scene = new EditorScene()
		@tools = new EditorTools()

return Editor