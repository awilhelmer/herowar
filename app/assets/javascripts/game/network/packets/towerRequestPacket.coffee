PacketType = require 'network/packets/packetType'
BasePacket = require 'network/packets/basePacket'

class TowerRequestPacket extends BasePacket
	
	constructor: (id, position) ->
		super PacketType.CLIENT_TOWER_REQUEST, { id: id, position: position }

return TowerRequestPacket