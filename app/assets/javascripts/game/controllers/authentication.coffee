RenderCanvasController = require 'controllers/rendererCanvas'
Variables = require 'variables'
log = require 'util/logger'
app = require 'application'

class AuthenticationController extends RenderCanvasController

	id: 'authentication'

	views:
		'views/authentication'	: ''

	initialize: (options) ->
		log.info 'Initialize authentication...'
		options = _.extend {}, options
		@token = options.token
		console.log options
		@authenticationComplete = false
		@alpha = 1.0
		@state = 1
		super options

	animate: =>
		requestAnimationFrame @animate unless @authenticationComplete
		@ctx.clearRect 0, 0, Variables.SCREEN_WIDTH, Variables.SCREEN_HEIGHT
		@ctx.save()
		if @state isnt 3
			@doAuthentication()
			@ctx.font = '24px Arial'
			@ctx.fillStyle = "rgba(200, 200, 200, #{@alpha})"
			@ctx.fillText "Authenticating", Variables.SCREEN_WIDTH / 2, Variables.SCREEN_HEIGHT / 2 + 30
			@ctx.restore()

	doAuthentication: ->
		switch @state
			when 1
				if app.socketClient.isOpen
					AuthPacket = require 'network/packets/authPacket'
					# TODO: get real auth token here
					packet = new AuthPacket @token
					app.socketClient.send packet
					@state = 2
					log.info 'Switched to state 2 !!!!'

return AuthenticationController