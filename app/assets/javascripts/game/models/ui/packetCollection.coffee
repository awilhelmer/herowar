events = require 'events'

class PacketCollection extends Backbone.Model

	bindPacketEvent: (type) ->
		events.on "retrieve:packet:#{type}", @onPacket, @	

	onPacket: (packet) ->
		values = _.extend _.omit(packet, 'type', 'createdTime'), { retrieveTime: packet.createdTime }
		delete values[key] for own key, value of values when value is null
		@parse values

return PacketCollection