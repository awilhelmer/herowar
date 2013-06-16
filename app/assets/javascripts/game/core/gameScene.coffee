WaveRequestPacket = require 'network/packets/waveRequestPacket'
PacketType = require 'network/packets/packetType'
scenegraph = require 'scenegraph'
Scene = require 'core/scene'
events = require 'events'
db = require 'database'

class GameScene extends Scene
	
	customId: 1000
	
	initialize: ->
		@addEventListeners()
		
	addEventListeners: ->
		events.on "retrieve:packet:#{PacketType.SERVER_CHAT_MESSAGE}", @onChatMessage, @
		events.on "retrieve:packet:#{PacketType.SERVER_OBJECT_OUT}", @onObjectOut, @
		events.on "retrieve:packet:#{PacketType.SERVER_TARGET_TOWER}", @onTowerTarget, @
		events.on "retrieve:packet:#{PacketType.SERVER_ATTACK_TOWER}", @onTowerAttack, @
		events.on "retrieve:packet:#{PacketType.SERVER_GAME_DEFEAT}", @onGameDefeat, @
		events.on "retrieve:packet:#{PacketType.SERVER_GAME_VICTORY}", @onGameVictory, @
		events.on 'call:wave', @onWaveCall, @
	
	onChatMessage: (packet) ->
		console.log 'Message: ', packet.message
		return
	
	onObjectOut: (packet) ->
		#console.log 'onObjectOut', packet.id
		obj = scenegraph.getDynObject packet.id
		setTimeout =>
			obj.kill() if obj
		, 500
		return
			
	onTowerTarget: (packet) ->
		tower = scenegraph.getDynObject packet.tower
		target = scenegraph.getDynObject packet.target
		tower.target = target if tower and target
		return
		
	onTowerAttack: (packet) ->
		owner = scenegraph.getDynObject packet.tower
		target = owner.target
		return unless owner and target
		owner.attack target, packet.damage unless target.isSoonDead()
		#console.log 'onTowerAttack', target.id, packet.damage, target.isSoonDead()
		return
	
	onWaveCall: (event) ->
		console.log 'onWaveCall...'
		events.trigger 'send:packet', new WaveRequestPacket()
		return
	
	onGameDefeat: ->
		@onFinish 'game/defeat'
		return

	onGameVictory: ->
		@onFinish 'game/victory'
		return
	
	onFinish: (url) ->
		stats = db.get 'ui/stats'
		stats.set '_freeze', true
		waves = db.get 'ui/waves'
		waves.set '_freeze', true
		Backbone.history.loadUrl url
		return
	
return GameScene