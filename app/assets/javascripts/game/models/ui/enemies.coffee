materialHelper = require 'helper/materialHelper'
PacketType = require 'network/packets/packetType'
PacketModel = require 'models/ui/packetModel'
scenegraph = require 'scenegraph'
Enemy = require 'models/enemy'
events = require 'events'
db = require 'database'

class Enemies extends PacketModel

	type: [ PacketType.SERVER_OBJECT_IN, PacketType.SERVER_OBJECT_OUT ]

	defaultValues:
		'_active' : true
		'current' : 0
		'quantity' : 0

	initialize: (options) ->
		super options

	onPacket: (packet) ->
		if packet 
			current = if @get('current') then @get('current') else 0
			if packet.type is PacketType.SERVER_OBJECT_IN
				quantity = if @get('quantity') then @get('quantity') else 0
				# TODO: we need here the info how much enemies are on the field for late joiner
				@createEnemy packet.id, packet.name, packet.path
				@set 
					'current': ++current
					'quantity': ++quantity
			else if packet.type is PacketType.SERVER_OBJECT_OUT
				@set 'current', --current
	
	createEnemy: (id, name, pathId) ->
		path = @getPathById pathId
		loadedData = db.data().geometries[name]
		dynObj = @createModel id, name, db.data().geometries[name], _.clone path.get 'waypoints'
		scenegraph.addDynObject dynObj, id
	
	createModel: (id, name, data, waypoints) ->
		model = new Enemy id, name, @createMesh id, name, data
		if _.isArray waypoints
			model.waypoints = waypoints
			model.root.position = new THREE.Vector3 waypoints[0].position.x, 0, waypoints[0].position.z
		model
		
	createMesh: (id, name, data) ->
		mesh = materialHelper.createAnimMesh data[0], data[1], name, id: id
		mesh.rotation.y = -90 * (Math.PI / 180)
		mesh
	
	getPathById: (id) ->
		allPaths = db.get 'paths'
		foundPaths = allPaths.where dbId : id
		unless foundPaths.length isnt 0 then throw "Couldnt find path for unit #{name}"
		return foundPaths[0]
		
return Enemies