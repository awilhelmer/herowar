PacketType = require 'network/packets/packetType'
BasePacket = require 'network/packets/basePacket'

class AuthPacket extends BasePacket
	
	constructor: (token) ->
		super PacketType.CLIENT_AUTH, { token: token }

return AuthPacket