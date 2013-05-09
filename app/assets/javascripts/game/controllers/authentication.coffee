RenderCanvasController = require 'controllers/rendererCanvas'
PacketType = require 'network/packets/packetType'
Variables = require 'variables'
log = require 'util/logger'
app = require 'application'
events = require 'events'
db = require 'database'

class AuthenticationController extends RenderCanvasController

	id: 'authentication'

	views:
		'views/authentication'	: ''

	initialize: (options) ->
		log.debug 'Initialize authentication process'
		options = _.extend {}, options
		@preload = db.get 'ui/preload'
		@token = options.token
		@state =
			complete	: false
			request		: false
			response	: false
			granted		: false
		@bindEvents()
		super options
	
	bindEvents: ->
		events.on "retrieve:packet:#{PacketType.SERVER_ACCESS_DECLINED}", @onAccessDenied, @
		events.on "retrieve:packet:#{PacketType.SERVER_ACCESS_GRANTED}", @onAccessGranted, @

	animate: =>
		requestAnimationFrame @animate unless @authenticationComplete
		@ctx.clearRect 0, 0, Variables.SCREEN_WIDTH, Variables.SCREEN_HEIGHT
		@ctx.save()
		@ctx.font = '24px Arial'
		@ctx.fillStyle = "rgba(200, 200, 200, 1.0)"
		if @state.request and @state.response and not @state.granted
			@ctx.fillText "Authentication Failed", Variables.SCREEN_WIDTH / 2, Variables.SCREEN_HEIGHT / 2 + 30
		else if not @state.request
			@sendAuthRequest()
			@ctx.fillText "Authenticating", Variables.SCREEN_WIDTH / 2, Variables.SCREEN_HEIGHT / 2 + 30
		@ctx.restore()
		@redirect() if @state.request and @state.response and @state.granted and not @state.complete and @preload.has 'map'

	sendAuthRequest: ->
		if not @state.request and app.socketClient.isOpen
				AuthPacket = require 'network/packets/authPacket'
				packet = new AuthPacket @token
				app.socketClient.send packet
				@state.request = true

	onAccessDenied: ->
		log.debug 'Access denied'
		@state.response = true

	onAccessGranted: ->
		log.debug 'Access granted'
		@state.granted = true
		@state.response = true

	redirect: ->
		@state.complete = true
		log.debug 'Authentication process completed'
		Backbone.history.loadUrl 'game2'

return AuthenticationController