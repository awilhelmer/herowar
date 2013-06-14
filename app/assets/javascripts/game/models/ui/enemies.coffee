PacketType = require 'network/packets/packetType'
PacketModel = require 'models/ui/packetModel'
enemiesFactory = require 'factory/enemies'
events = require 'events'

class Enemies extends PacketModel

	type: [ PacketType.SERVER_OBJECT_IN, PacketType.SERVER_OBJECT_OUT ]

	defaultValues:
		'_active' : true
		'current' : 0
		'quantity' : 0

	onPacket: (packet) ->
		if packet 
			current = if @get('current') then @get('current') else 0
			if packet.type is PacketType.SERVER_OBJECT_IN
				quantity = if @get('quantity') then @get('quantity') else 0
				enemiesFactory.create packet
				@set 
					'current': ++current
					'quantity': ++quantity
			else if packet.type is PacketType.SERVER_OBJECT_OUT
				@set 'current', --current
		return
		
return Enemies