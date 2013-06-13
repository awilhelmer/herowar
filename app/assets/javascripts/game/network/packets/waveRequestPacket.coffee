PacketType = require 'network/packets/packetType'
BasePacket = require 'network/packets/basePacket'

class WaveRequestPacket extends BasePacket
	
	constructor: (id, position) ->
		super PacketType.CLIENT_WAVE_REQUEST

return WaveRequestPacket