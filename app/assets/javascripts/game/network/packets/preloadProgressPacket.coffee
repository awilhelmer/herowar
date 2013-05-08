PacketType = require 'network/packets/packetType'
BasePacket = require 'network/packets/basePacket'

class PreloadProgressPacket extends BasePacket
  
  constructor: ->
    super PacketType.CLIENT_PRELOAD_PROGRESS

return PreloadProgressPacket