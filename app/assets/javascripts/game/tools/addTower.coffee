TowerRequestPacket = require 'network/packets/towerRequestPacket'
PacketType = require 'network/packets/packetType'
AddObject = require 'tools/addObject'
scenegraph = require 'scenegraph'
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
		#mesh.rotation.x = THREE.Math.degToRad -180
		model = new Tower packet.objectId, name, mesh
		model.root.position.set packet.position.x, packet.position.y, packet.position.z
		model.active = true
		#model.showRange()
		scenegraph.addDynObject model, packet.objectId

	onLeaveTool: ->
		model = @tool.get 'currentObject'
		if model
			model.dispose()
			@tool.unset 'currentObject'

	addMesh: ->
		model = @tool.get 'currentObject'
		scenegraph.addDynObject model, @tool.get 'currentObjectId'

	placeMesh: ->
		console.log 'Place tower', @tool.get('currentObject').root
		events.trigger 'send:packet', new TowerRequestPacket 1, @tool.get('currentObject').root.position # TODO: fix hardcoded tower id
	
	onLoadGeometry: (geometry, materials, json) =>
		unless json
			json = _.extend id: @tool.get('currentObjectId'), json 
		mesh = @createMesh geometry, materials, @tool.get('currentObjectName'), json
		#mesh.rotation.x = THREE.Math.degToRad -180
		model = new Tower @tool.get('currentObjectId'), name, mesh
		model.showRange()
		model.root.visible = false
		@tool.set 'currentObject', model
		@addMesh()

	onNonIntersect: ->
		model = @tool.get 'currentObject'
		model.root.visible = false if model.root.visible

	update: (position, intersect) ->
		model = @tool.get 'currentObject'
		if model
			position.y = 0	# TODO: fix this hotfix (positive y values hide the tower...)
			model.root.position = position
			model.root.visible = true unless model.root.visible

	onMouseUp: (event) ->
		@placeMesh() if @tool.get('currentObject')?.root.visible and !@input.get('mouse_moved') if event.which is 1
	
return AddTowerTool