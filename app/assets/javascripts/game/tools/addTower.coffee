TowerRequestPacket = require 'network/packets/towerRequestPacket'
PacketType = require 'network/packets/packetType'
AddObject = require 'tools/addObject'
Tower = require 'models/tower'
events = require 'events'
db = require 'database'

class AddTowerTool extends AddObject
	
	internalId: 200
	
	bindEvents: ->
		events.on 'select:tower', @onSelectTower, @
		events.on "retrieve:packet:#{PacketType.SERVER_BUILD_TOWER}", @onBuildTower, @
	
	onSelectTower: (id) ->
		name = "tower#{id}"
		data = db.data().geometries[name]
		console.log 'Found geometry', data
		@tool.set
			'currentObjectId' 	: @internalId++
			'currentObjectName'	: name
		@onLoadGeometry data[0], data[1], data[2]

	onBuildTower: (packet) ->
		console.log 'onBuildTower()', packet
		name = "tower#{packet.towerId}"
		data = db.data().geometries[name]
		obj = @createThreeObject data[0], data[1], name, data[2]
		obj.position.set packet.position.x, packet.position.y, packet.position.z
		model = new Tower packet.objectId, name, obj
		events.trigger 'add:dynamicObject', packet.objectId, model

	addMesh: ->
		mesh = @tool.get('currentObject')
		@app.engine.scenegraph.addStaticObject mesh, @tool.get('currentObjectId')

	placeMesh: ->
		console.log 'Place tower', @tool.get('currentObject').position
		events.trigger 'send:packet', new TowerRequestPacket 1, @tool.get('currentObject').position # TODO: fix hardcoded tower id

	# TODO: SHOULD BE REMOVED LATER ON !!!
	
	onLoadGeometry: (geometry, materials, json) =>
		super geometry, materials, json
		console.log "Mesh:", @tool.get 'currentObject'
	
	update: (position, intersect) ->
		position.y = 0	# TODO: fix this hotfix (positive y values hide the tower...)
		#console.log 'Update', position, intersect
		super position, intersect		
	
return AddTowerTool