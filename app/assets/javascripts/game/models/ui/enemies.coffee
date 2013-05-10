PacketType = require 'network/packets/packetType'
PacketModel = require 'models/ui/packetModel'

class Enemies extends PacketModel

	type: PacketType.SERVER_OBJECT_IN

	defaultValues:
		'_active' : true
		'quantity' : 0

	onPacket: (packet) ->
		if packet
			# TODO: we need here the info how much enemies are on the field for late joiner
			quantity = if @get('quantity') then @get('quantity') else 0
			quantity++
			@set 'quantity', quantity

return Enemies