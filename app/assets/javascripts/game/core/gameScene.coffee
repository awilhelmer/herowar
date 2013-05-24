PacketType = require 'network/packets/packetType'
scenegraph = require 'scenegraph'
Scene = require 'core/scene'
Shot = require 'models/shot'
events = require 'events'

class GameScene extends Scene
	
	customId: 1000
	
	initialize: ->
		@addEventListeners()
		
	addEventListeners: ->
		events.on "retrieve:packet:#{PacketType.SERVER_OBJECT_OUT}", @onObjectOut, @
		events.on "retrieve:packet:#{PacketType.SERVER_TARGET_TOWER}", @onTowerTarget, @
		events.on "retrieve:packet:#{PacketType.SERVER_ATTACK_TOWER}", @onTowerAttack, @
	
	onObjectOut: (packet) ->
		#console.log 'onObjectOut', scenegraph.dynamicObjects[packet.id]
		scenegraph.removeDynObject packet.id
			
	onTowerTarget: (packet) ->
		scenegraph.dynamicObjects[packet.tower].target = scenegraph.dynamicObjects[packet.target]
		#console.log 'onTowerTarget', scenegraph.dynamicObjects[packet.tower], "target now", scenegraph.dynamicObjects[packet.target]
		
	onTowerAttack: (packet) ->
		owner = scenegraph.dynamicObjects[packet.tower]
		target = scenegraph.dynamicObjects[packet.tower].target
		shot = new Shot @customId++, owner, target, packet.damage
		scenegraph.addDynObject shot, shot.id
		scenegraph.dynamicObjects[packet.tower].target.hit packet.damage
		currHealth = scenegraph.dynamicObjects[packet.tower].target.currentHealth
		currHealth = 0 if currHealth < 0
		percentage = Math.round currHealth / scenegraph.dynamicObjects[packet.tower].target.maxHealth * 100
		#console.log 'onTowerTarget', scenegraph.dynamicObjects[packet.tower], "hit target", scenegraph.dynamicObjects[packet.tower].target, "for", packet.damage, "Percent", percentage 
	
return GameScene