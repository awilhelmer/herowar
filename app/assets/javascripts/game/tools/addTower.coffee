TowerRequestPacket = require 'network/packets/towerRequestPacket'
PacketType = require 'network/packets/packetType'
AddObject = require 'tools/addObject'
scenegraph = require 'scenegraph'
towers = require 'factory/towers'
events = require 'events'
db = require 'database'

class AddTowerTool extends AddObject
	
	internalId: 200
	
	bindEvents: ->
		events.on 'select:tower', @onSelectTower, @
		events.on "retrieve:packet:#{PacketType.SERVER_BUILD_TOWER}", @onBuildTower, @
	
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
		model.active = true
		model.range = tower.get 'coverage'
		model.reload = tower.get 'reload'
		model.weapons = tower.get 'weapons'
		return

	onLeaveTool: ->
		@_removeObject()
		return

	addMesh: ->

	placeMesh: ->
		console.log 'Place tower', @tool.get('currentObject').getMainObject()
		events.trigger 'send:packet', new TowerRequestPacket @towerId, @tool.get('currentObject').getMainObject().position
	
	onLoadGeometry: (geometry, materials, json) =>
		tower = db.get 'db/towers', @towerId
		model = towers.create
			id: @tool.get 'currentObjectId'
			name: tower.get 'name'
		model.range = tower.get 'coverage'
		model.showRange()
		model.visible false
		@tool.set 'currentObject', model
		console.log 'onLoadGeometry', model
		@addMesh()

	onNonIntersect: ->
		model = @tool.get 'currentObject'
		model.visible false if model.visible()

	update: (position, intersect) ->
		model = @tool.get 'currentObject'
		if model
			# position.y = 200
			model.getMainObject().position = position
			model.visible true unless model.visible()

	onMouseUp: (event) ->
		@placeMesh() if @tool.get('currentObject')?.visible() and !@input.get('mouse_moved') if event.which is 1

	_removeObject: ->
		model = @tool.get 'currentObject'
		if model
			model.dispose()
			@tool.unset 'currentObject'
		return
	
return AddTowerTool