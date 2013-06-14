PacketCollection = require 'models/ui/packetCollection'
PacketType = require 'network/packets/packetType'

class Chat extends PacketCollection

	type: [ PacketType.SERVER_CHAT_MESSAGE ]

return Chat