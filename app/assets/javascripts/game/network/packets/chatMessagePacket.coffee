PacketType = require 'network/packets/packetType'
BasePacket = require 'network/packets/basePacket'

class ChatMessagePacket extends BasePacket
	
	constructor: (message) ->
		super PacketType.CLIENT_CHAT_MESSAGE, { message: message }

return ChatMessagePacket