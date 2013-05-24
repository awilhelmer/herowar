PacketType = require 'network/packets/packetType'
PacketModel = require 'models/ui/packetModel'
events = require 'events'

class Stats extends PacketModel

	type: [ PacketType.SERVER_PLAYER_INIT_STATS, PacketType.SERVER_PLAYER_UPDATE_STATS ]

	timeValues:
		'gold' : 'goldPerTick'

	bindPacketEvents: ->
		super()
		events.on "retrieve:packet:#{PacketType.SERVER_PLAYER_UPDATE_LIVES}", @onUpdateLives, @

	onUpdateLives: (packet) ->
		@set 'lives', packet.lives

return Stats