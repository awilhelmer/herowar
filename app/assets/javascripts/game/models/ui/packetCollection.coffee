events = require 'events'

class PacketCollection extends Backbone.Collection

	initialize: (options) ->
		super options
		@bindPacketEvents()

	bindPacketEvents: ->
		if not _.isNull(@type) and _.isArray @type
			@bindPacketEvent t for t in @type
		else if not _.isNull @type
			@bindPacketEvent @type
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