WaveRequestPacket = require 'network/packets/waveRequestPacket'
PacketType = require 'network/packets/packetType'
enemiesFactory = require 'factory/enemies'
scenegraph = require 'scenegraph'
Scene = require 'core/scene'
events = require 'events'
db = require 'database'

class GameScene extends Scene
		
	initialize: ->
		@enemyWaypointTargets = []
		@towerAreaRescritions = []
		@addEventListeners()
		return
		
	addEventListeners: ->
		events.on "retrieve:packet:#{PacketType.SERVER_UNIT_IN}", @onUnitIn, @
		events.on "retrieve:packet:#{PacketType.SERVER_UNIT_OUT}", @onUnitOut, @
		events.on "retrieve:packet:#{PacketType.SERVER_TARGET_TOWER}", @onTowerTarget, @
		events.on "retrieve:packet:#{PacketType.SERVER_ATTACK_TOWER}", @onTowerAttack, @
		events.on "retrieve:packet:#{PacketType.SERVER_TOWER_RESTRICTION}", @_onTowerRestriction, @
		events.on "retrieve:packet:#{PacketType.SERVER_GAME_DEFEAT}", @onGameDefeat, @
		events.on "retrieve:packet:#{PacketType.SERVER_GAME_VICTORY}", @onGameVictory, @
		events.on "retrieve:packet:#{PacketType.SERVER_GUI_UPDATE}", @_onGUIUpdate, @
		events.on 'scene:created', @_onSceneCreated, @
		events.on 'call:wave', @onWaveCall, @
		return
		
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
		return unless owner
		target = owner.target
		return unless target
		owner.attack target, packet.damage unless target.isSoonDead()
		return
	
	_onTowerRestriction: (packet) ->
		console.log '_onTowerRestriction', packet
		unless @towerAreaRescritions.length is 0
			res = @towerAreaRescritions[0]
			scenegraph.scene().remove res
			@towerAreaRescritions.length = 0
		return unless packet.position
		outerRadius = packet.radius + 2
		material = new THREE.MeshBasicMaterial color: '#000000', opacity: 0.75, transparent: true
		geometry = new THREE.RingGeometry packet.radius, outerRadius, outerRadius * 2, packet.radius * 2
		target = new THREE.Mesh geometry, material
		target.name = 'towerAreaRestriction'
		target.position.copy packet.position
		target.rotation.x = THREE.Math.degToRad -90
		@towerAreaRescritions.push target
		scenegraph.scene().add target
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

	_onSceneCreated: ->
		paths = db.get 'db/paths'
		@_createEnemyWaypointTarget path for path in paths.models when path.get('waypoints')?.length isnt 0
		return

	_onGUIUpdate: (packet) ->
		console.log '_onGUIUpdate', packet
		$guiElement = $ "##{packet.name}"
		if $guiElement.length is 1
			$guiElement.css 'display', '' if packet.visible
			$guiElement.css 'display', 'none' unless packet.visible
		return

	_createEnemyWaypointTarget: (path) ->
		position = path.get('waypoints')[path.get('waypoints').length - 1].position
		return for pos in @enemyWaypointTargets when pos.x is position.x and pos.y is position.y and pos.z is position.z				
		@enemyWaypointTargets.push position
		innerRadius = 5
		outerRadius = innerRadius + 2
		material = new THREE.MeshBasicMaterial color: '#3366FF', opacity: 0.75, transparent: true
		geometry = new THREE.RingGeometry innerRadius, outerRadius, outerRadius * 2, innerRadius * 2
		target = new THREE.Mesh geometry, material
		target.name = "enemyWaypointTarget-#{path.get('id')}"
		target.position.copy position
		target.rotation.x = THREE.Math.degToRad -90
		scenegraph.scene().add target
		return

return GameScene