PacketType = require 'network/packets/packetType'
PacketModel = require 'models/ui/packetModel'

class Enemies extends PacketModel

	types: [ PacketType.SERVER_UNIT_IN, PacketType.SERVER_UNIT_OUT ]

	defaultValues:
		'_active' : true
		'current' : 0
		'quantity' : 0

	onPacket: (packet) ->
		if packet 
			current = if @get('current') then @get('current') else 0
			if packet.type is PacketType.SERVER_UNIT_IN
				quantity = if @get('quantity') then @get('quantity') else 0
				@set 
					'current': ++current
					'quantity': ++quantity
			else if packet.type is PacketType.SERVER_UNIT_OUT
				@set 'current', --current
		return
		
return Enemies