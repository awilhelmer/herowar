RenderCanvasController = require 'controllers/rendererCanvas'
Variables = require 'variables'
log = require 'util/logger'

class AuthenticationController extends RenderCanvasController

	id: 'authentication'

	views:
		'views/authentication'	: ''

	initialize: (options) ->
		log.info 'Initialize authentication...'
		@authenticationComplete = false
		@alpha = 1.0
		@state = 1
		super options

	animate: =>
		requestAnimationFrame @animate unless @authenticationComplete
		@ctx.clearRect 0, 0, Variables.SCREEN_WIDTH, Variables.SCREEN_HEIGHT
		@ctx.save()
		if @state is 1
			@ctx.font = '24px Arial'
			@ctx.fillStyle = "rgba(200, 200, 200, #{@alpha})"
			@ctx.fillText "Authenticating", Variables.SCREEN_WIDTH / 2, Variables.SCREEN_HEIGHT / 2 + 30
			@ctx.restore()

return AuthenticationController