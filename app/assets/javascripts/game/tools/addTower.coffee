TowerRequestPacket = require 'network/packets/towerRequestPacket'
PacketType = require 'network/packets/packetType'
AddObject = require 'tools/addObject'
scenegraph = require 'scenegraph'
towers = require 'factory/towers'
events = require 'events'
db = require 'database'

class AddTowerTool extends AddObject
	
	internalId: 45000
	
	bindEvents: ->
		events.on 'select:tower', @onSelectTower, @
		events.on "retrieve:packet:#{PacketType.SERVER_BUILD_TOWER}", @onBuildTower, @
		events.on "retrieve:packet:#{PacketType.SERVER_TOWER_REJECTED}", @onBuildRejected, @
		return
	
	onSelectTower: (id) ->
		@_removeObject()
		tower = db.get 'db/towers', id
		name = tower.get 'name'
		data = db.data().geometries[name]
		@towerId = id
		@tool.set
			'currentObjectId' 	: @internalId++
			'currentObjectName'	: name
		@onLoadGeometry data[0], data[1], data[2]
		return

	onBuildTower: (packet) ->
		console.log 'onBuildTower()', packet
		tower = db.get 'db/towers', packet.towerId
		model = towers.create
			id: packet.objectId
			name: tower.get 'name'
			position:
				x: packet.position.x
				y: packet.position.y
				z: packet.position.z
			rotationSpeed: tower.get 'rotationSpeed'
			owner: id: packet.playerId, name: packet.playerName
		model.active = true
		model.range = tower.get 'coverage'
		model.reload = tower.get 'reload'
		model.weapons = tower.get 'weapons'
		return

	onBuildRejected: (packet) ->
		console.log 'onBuildRejected()', packet
		obj = @tool.get 'currentObject'
		obj.showGlow 250
		return

	onLeaveTool: ->
		@_removeObject()
		return

	addMesh: ->
		return

	placeMesh: ->
		obj = @tool.get 'currentObject'
		position = obj.getMainObject().position
		console.log 'Request placing tower', obj.name, position
		events.trigger 'send:packet', new TowerRequestPacket @towerId, position
		return
	
	onLoadGeometry: (geometry, materials, json) =>
		tower = db.get 'db/towers', @towerId
		model = towers.create
			id: @tool.get 'currentObjectId'
			name: tower.get 'name'
		model.glowColor = 0xff0000
		model.range = tower.get 'coverage'
		model.showRange()
		model.visible false
		@tool.set 'currentObject', model
		console.log 'onLoadGeometry', model
		@addMesh()
		return

	onNonIntersect: ->
		model = @tool.get 'currentObject'
		model.visible false if model.visible()
		return

	update: (position, intersect) ->
		model = @tool.get 'currentObject'
		return unless model
		model.position position
		model.visible true unless model.visible()
		return

	onMouseUp: (event) ->
		@placeMesh() if @tool.get('currentObject')?.visible() and !@input.get('mouse_moved') if event.which is 1
		return

	_removeObject: ->
		model = @tool.get 'currentObject'
		return unless model
		model.dispose()
		@tool.unset 'currentObject'
		return
	
return AddTowerTool