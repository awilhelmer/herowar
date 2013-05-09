PacketType = require 'network/packets/packetType'
BasePacket = require 'network/packets/basePacket'

class PreloadUpdatePacket extends BasePacket
	
	constructor: (progress) ->
		super PacketType.CLIENT_PRELOAD_UPDATE, { progress: progress }

return PreloadUpdatePacket