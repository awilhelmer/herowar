PacketType = require 'network/packets/packetType'
events = require 'events'

class Preload extends Backbone.Model

	initialize: (options) ->
		super options
		@bindEvents()

	bindEvents: ->
		events.on "retrieve:packet:#{PacketType.SERVER_PRELOAD_DATA}", @updateAttributes, @
		
	updateAttributes: (packet) ->
		if packet
			_.extend @attributes, _.omit packet, 'type', 'createdTime'
			@trigger 'change'

return Preload