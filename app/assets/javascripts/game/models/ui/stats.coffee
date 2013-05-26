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

	onPacket: (packet) ->
		super packet
		events.trigger 'stats:score:changed', (if packet.changedScore > 0 then "+#{packet.changedScore}" else "#{packet.changedScore}") if packet.changedScore
		events.trigger 'stats:lives:changed', (if packet.changedLives > 0 then "+#{packet.changedLives}" else "#{packet.changedLives}") if packet.changedLives
		events.trigger 'stats:gold:changed', (if packet.changedGold > 0 then "+#{packet.changedGold}" else "#{packet.changedGold}") if packet.changedGold

	onUpdateLives: (packet) ->
		changed = @get('lives') - packet.lives
		@set 'lives', packet.lives
		events.trigger 'stats:lives:changed', "-#{changed}"

return Stats