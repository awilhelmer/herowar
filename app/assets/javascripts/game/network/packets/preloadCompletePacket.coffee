PacketType = require 'network/packets/packetType'
BasePacket = require 'network/packets/basePacket'

class PreloadCompletePacket extends BasePacket
	
	constructor: ->
		super PacketType.CLIENT_PRELOAD_COMPLETE

return PreloadCompletePacket