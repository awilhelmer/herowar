PacketType = require 'network/packets/packetType'
BasePacket = require 'network/packets/basePacket'

class TutorialUpdatePacket extends BasePacket
	
	constructor: ->
		super PacketType.CLIENT_TUTORIAL_UPDATE, {}

return TutorialUpdatePacket