PacketCollection = require 'models/ui/packetCollection'
PacketType = require 'network/packets/packetType'

class Chat extends PacketCollection

	types: PacketType.SERVER_CHAT_MESSAGE

return Chat