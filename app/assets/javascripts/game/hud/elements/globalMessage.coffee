BaseHUDElement = require 'hud/elements/baseHudElement'
PacketType = require 'network/packets/packetType'
events = require 'events'

class GlobalMessageHUDElement extends BaseHUDElement

	initialize: ->
		@messages = []
		@alpha = 0.0
		@bindEvents()
		return
	
	bindEvents: ->
		events.on "retrieve:packet:#{PacketType.SERVER_GLOBAL_MESSAGE}", @_addMessage, @
		return

	update: (delta, now) ->
		return if @messages.length is 0
		@_drawMessage delta, now
		@_updateAlpha delta, now
		return
	
	_drawMessage: (delta, now) ->
		@ctx.fillStyle = "rgba(255, 255, 255, #{@alpha})"
		@ctx.font = 'bold 22px Arial'
		@ctx.fillText @messages[0], @canvas.width * 0.5, @canvas.height * 0.25
		return

	_updateAlpha: (delta, now) ->
		if @changeState
			@alpha -= delta * 2
			if @alpha <= 0
				@alpha = 0
				@changeState = false
				@messages.splice 0, 1
		else
			if @alpha < 1.0
				@alpha += delta * 2
				if @alpha >= 1.0
					@alpha = 1.0 
					@changeState = true
		return

	_addMessage: (packet) ->
		console.log '_addMessage', packet
		@messages.push packet.message
		return
	
return GlobalMessageHUDElement