RenderCanvasController = require 'controllers/rendererCanvas'

class AuthenticationController extends RenderCanvasController

	id: 'authentication'

	views:
		'views/authentication'	: ''

	initialize: (options) ->
		log.info 'Initialize authentication...'		
		super options

return AuthenticationController