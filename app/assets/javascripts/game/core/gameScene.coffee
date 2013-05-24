PacketType = require 'network/packets/packetType'
scenegraph = require 'scenegraph'
Scene = require 'core/scene'
events = require 'events'

class GameScene extends Scene
	
	initialize: ->
		@addEventListeners()
		
	addEventListeners: ->
		events.on "retrieve:packet:#{PacketType.SERVER_TARGET_TOWER}", @onTowerTarget, @
	
	onTowerTarget: (packet) ->
		scenegraph.dynamicObjects[packet.tower].target = scenegraph.dynamicObjects[packet.target]
		console.log 'onTowerTarget', scenegraph.dynamicObjects[packet.tower], "target now", scenegraph.dynamicObjects[packet.target]
	
return GameScene