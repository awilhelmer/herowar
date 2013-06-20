events = require 'events'

class PacketCollection extends Backbone.Collection

	types: null

	initialize: (options) ->
		super options
		@bindPacketEvents()

	bindPacketEvents: ->
		if not _.isNull(@types) and _.isArray @types
			@bindPacketEvent t for t in @types
		else if not _.isNull @types
			@bindPacketEvent @types
		return
	
	bindPacketEvent: (type) ->
		events.on "retrieve:packet:#{type}", @onPacket, @
		return

	onPacket: (packet) ->
		values = _.extend _.omit(packet, 'type', 'createdTime'), { retrieveTime: packet.createdTime }
		delete values[key] for own key, value of values when value is null
		console.log 'Parse message:', values
		@add values
		return

return PacketCollection