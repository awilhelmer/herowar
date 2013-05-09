PacketType = require 'network/packets/packetType'
PacketModel = require 'models/ui/packetModel'
log = require 'util/logger'

class Waves extends PacketModel

	type: PacketType.SERVER_WAVES_STATUS

	update: =>
		log.debug 'Update waves model'

return Waves