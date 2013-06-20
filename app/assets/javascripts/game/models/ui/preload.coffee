PacketType = require 'network/packets/packetType'
PacketModel = require 'models/ui/packetModel'

class Preload extends PacketModel

	types: PacketType.SERVER_PRELOAD_DATA
	
	rate: 0

return Preload