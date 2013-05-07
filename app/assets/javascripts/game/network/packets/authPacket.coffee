PacketType = require 'network/packets/packetType'
BasePacket = require 'network/packets/basePacket'

class AuthPacket extends BasePacket
	
	constructor: (token) ->
		super PacketType.INIT, { token: token }

return AuthPacket