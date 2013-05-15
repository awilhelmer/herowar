TowerRequestPacket = require 'network/packets/towerRequestPacket'
AddObject = require 'tools/addObject'
events = require 'events'
db = require 'database'

class AddTowerTool extends AddObject
	
	internalId: 200
	
	bindEvents: ->
		events.on 'select:tower', @onSelectTower, @
	
	onSelectTower: (id) ->
		name = "tower#{id}"
		data = db.data().geometries[name]
		console.log 'Found geometry', data
		@tool.set
			'currentObjectId' 	: @internalId++
			'currentObjectName'	: name
		@onLoadGeometry data[0], data[1], data[2]

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
		position.y = 10	# TODO: fix this hotfix (positive y values hide the tower...)
		console.log 'Update', position, intersect
		super position, intersect		
	
return AddTowerTool