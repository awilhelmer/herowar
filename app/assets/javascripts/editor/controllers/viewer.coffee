ApplicationController = require 'controllers/application'
Variables = require 'variables'
log = require 'util/logger'

class ViewerController extends ApplicationController

	views:
		'views/viewer' : ''

	viewports: [
		domId: '#viewer',
		type: Variables.VIEWPORT_TYPE_EDITOR
		camera:
			type: Variables.CAMERA_TYPE_PERSPECTIVE
			position: [ 300, 150, 300 ]	
	]

	initialize: (options) ->
		log.info 'Initialize viewer...'
		super options
		
	initEngine: ->
		engine = super()
		engine.pause = true

return ViewerController