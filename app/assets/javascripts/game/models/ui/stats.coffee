PacketType = require 'network/packets/packetType'
PacketModel = require 'models/ui/packetModel'
log = require 'util/logger'

class Stats extends PacketModel

	type: [ PacketType.SERVER_PLAYER_INIT_STATS, PacketType.SERVER_PLAYER_UPDATE_STATS ]

	timeValues:
		'gold' : 'goldPerTick'

return Stats