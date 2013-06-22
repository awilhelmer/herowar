WaveRequestPacket = require 'network/packets/waveRequestPacket'
PacketType = require 'network/packets/packetType'
enemiesFactory = require 'factory/enemies'
scenegraph = require 'scenegraph'
Scene = require 'core/scene'
events = require 'events'
db = require 'database'

class GameScene extends Scene
	
	customId: 1000
	
	initialize: ->
		@addEventListeners()
		
	addEventListeners: ->
		events.on "retrieve:packet:#{PacketType.SERVER_UNIT_IN}", @onUnitIn, @
		events.on "retrieve:packet:#{PacketType.SERVER_UNIT_OUT}", @onUnitOut, @
		events.on "retrieve:packet:#{PacketType.SERVER_TARGET_TOWER}", @onTowerTarget, @
		events.on "retrieve:packet:#{PacketType.SERVER_ATTACK_TOWER}", @onTowerAttack, @
		events.on "retrieve:packet:#{PacketType.SERVER_GAME_DEFEAT}", @onGameDefeat, @
		events.on "retrieve:packet:#{PacketType.SERVER_GAME_VICTORY}", @onGameVictory, @
		events.on "retrieve:packet:#{PacketType.SERVER_GUI_UPDATE}", @_onGUIUpdate, @
		events.on 'call:wave', @onWaveCall, @
		
	onUnitIn: (packet) ->
		enemiesFactory.create packet
		return
	
	onUnitOut: (packet) ->
		victim = scenegraph.getDynObject packet.id
		unless packet.killedBy is null
			killer = scenegraph.getDynObject packet.killedBy
			killer.attributes.kills += 1
			killer.attributes.rewardGold += packet.rewardGold
		setTimeout =>
			victim.kill() if victim
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
		return
	
	onWaveCall: (event) ->
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

	_onGUIUpdate: (packet) ->
		console.log '_onGUIUpdate', packet
		$guiElement = $ "##{packet.name}"
		if $guiElement.length is 1
			$guiElement.css 'display', '' if packet.visible
			$guiElement.css 'display', 'none' unless packet.visible
		return
	
return GameScene