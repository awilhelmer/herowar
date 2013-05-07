PacketType = require 'network/packets/packetType'
BasePacket = require 'network/packets/basePacket'

class PreloadCompletePacket extends BasePacket
	
	constructor: ->
		super PacketType.PRELOAD_COMPLETE

return PreloadCompletePacket