EditorEventbus = require 'editorEventbus'
ApplicationController = require 'controllers/application'
EditorScene = require 'core/editorScene'
Input = require 'core/input'
Tools = require 'core/tools'
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

	initialize: (options) ->
		log.info 'Initialize editor...'
		super options
		@bindEvents()
		
	initEngine: ->
		super()
		@engine.pause = true
	
	initCore: ->
		@input = new Input @
		@tools = new Tools @
		@scene = new EditorScene @

	bindEvents: ->
		EditorEventbus.render.add @render
	
	render: (resize) =>
		if resize then @engine.onWindowResize true else	@engine.render()

return Editor