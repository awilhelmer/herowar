PacketType = require 'network/packets/packetType'
PacketModel = require 'models/ui/packetModel'
log = require 'util/logger'

class Stats extends PacketModel

	type: PacketType.SERVER_PLAYER_STATUS

	update: =>
		log.debug 'Update states model'

return Stats