PacketType = require 'network/packets/packetType'
scenegraph = require 'scenegraph'
Scene = require 'core/scene'
Laser = require 'models/laser'
events = require 'events'
db = require 'database'

class GameScene extends Scene
	
	customId: 1000
	
	initialize: ->
		@addEventListeners()
		
	addEventListeners: ->
		events.on "retrieve:packet:#{PacketType.SERVER_OBJECT_OUT}", @onObjectOut, @
		events.on "retrieve:packet:#{PacketType.SERVER_TARGET_TOWER}", @onTowerTarget, @
		events.on "retrieve:packet:#{PacketType.SERVER_ATTACK_TOWER}", @onTowerAttack, @
		events.on "retrieve:packet:#{PacketType.SERVER_GAME_DEFEAT}", @onGameDefeat, @
		events.on "retrieve:packet:#{PacketType.SERVER_GAME_VICTORY}", @onGameVictory, @
	
	onObjectOut: (packet) ->
		#console.log 'onObjectOut', scenegraph.getDynObject(packet.id)
		#scenegraph.removeDynObject packet.id
			
	onTowerTarget: (packet) ->
		tower = scenegraph.getDynObject packet.tower
		target = scenegraph.getDynObject packet.target
		tower.target = target if tower and target
		#console.log 'onTowerTarget', scenegraph.getDynObject(packet.tower), "target now", scenegraph.getDynObject(packet.target)
		
	onTowerAttack: (packet) ->
		owner = scenegraph.getDynObject packet.tower
		target = owner.target
		return unless owner and target
		
		laser1 = new Laser @customId++, owner, target, packet.damage
		laser1.getMainObject().position.x += 12
		scenegraph.addDynObject laser1, laser1.id
		
		laser2 = new Laser @customId++, owner, target, packet.damage
		laser2.getMainObject().position.x -= 12
		scenegraph.addDynObject laser2, laser2.id
		
		unless target.isDead()
			target.hit packet.damage
			currHealth = target.currentHealth
			currHealth = 0 if currHealth < 0
			percentage = Math.round currHealth / target.maxHealth * 100
			#console.log 'onTowerTarget', scenegraph.getDynObject(packet.tower), "hit target", scenegraph.getDynObject(packet.tower).target, "for", packet.damage, "Percent", percentage 

	onGameDefeat: ->
		@onFinish 'defeat'
		return

	onGameVictory: ->
		@onFinish 'victory'
		return
	
	onFinish: (url) ->
		stats = db.get 'ui/stats'
		stats.set '_freeze', true
		waves = db.get 'ui/waves'
		waves.set '_freeze', true
		Backbone.history.loadUrl url
		return
	
return GameScene