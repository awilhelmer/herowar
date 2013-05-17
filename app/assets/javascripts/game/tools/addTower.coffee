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
		mesh = @createMesh data[0], data[1], name, data[2]
		mesh.position.set packet.position.x, packet.position.y, packet.position.z
		model = new Tower packet.objectId, name, mesh
		model.active = true
		model.showRange()
		events.trigger 'add:dynamicObject', packet.objectId, model

	onLeaveTool: ->
		model = @tool.get 'currentObject'
		if model
			model.dispose()
			@tool.unset 'currentObject'

	addMesh: ->
		model = @tool.get 'currentObject'
		events.trigger 'add:dynamicObject', @tool.get('currentObjectId'), model

	placeMesh: ->
		console.log 'Place tower', @tool.get('currentObject').object3d.position
		events.trigger 'send:packet', new TowerRequestPacket 1, @tool.get('currentObject').object3d.position # TODO: fix hardcoded tower id
	
	onLoadGeometry: (geometry, materials, json) =>
		unless json
			json = _.extend id: @tool.get('currentObjectId'), json 
		mesh = @createMesh geometry, materials, @tool.get('currentObjectName'), json
		model = new Tower @tool.get('currentObjectId'), name, mesh
		model.showRange()
		model.object3d.visible = false
		@tool.set 'currentObject', model
		@addMesh()
		console.log "Model:", model

	onNonIntersect: ->
		model = @tool.get 'currentObject'
		model.object3d.visible = false if model.object3d.visible

	update: (position, intersect) ->
		model = @tool.get 'currentObject'
		if model
			position.y = 0	# TODO: fix this hotfix (positive y values hide the tower...)
			model.object3d.position = position
			model.object3d.visible = true unless model.object3d.visible

	onMouseUp: (event) ->
		@placeMesh() if @tool.get('currentObject')?.object3d.visible and !@input.get('mouse_moved') if event.which is 1
	
return AddTowerTool