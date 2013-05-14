materialHelper = require 'helper/materialHelper'
PacketType = require 'network/packets/packetType'
PacketModel = require 'models/ui/packetModel'
Enemy = require 'models/enemy'
events = require 'events'
db = require 'database'

class Enemies extends PacketModel

	type: PacketType.SERVER_OBJECT_IN

	defaultValues:
		'_active' : true
		'quantity' : 0

	initialize: (options) ->
		super options

	onPacket: (packet) ->
		if packet
			# TODO: we need here the info how much enemies are on the field for late joiner
			quantity = if @get('quantity') then @get('quantity') else 0
			quantity++
			@set 'quantity', quantity
			@createEnemy packet.id, packet.name, packet.path
	
	createEnemy: (id, name, pathId) ->
		path = @getPathById pathId
		loadedData = db.data().geometries[name]
		dynObj = @createModel id, name, db.data().geometries[name], _.clone path.get 'waypoints'
		events.trigger 'add:dynamicObject', id, dynObj
	
	createModel: (id, name, data, waypoints) ->
		model = new Enemy id, name, @createMesh id, name, data
		if _.isArray waypoints
			model.waypoints = waypoints
			model.object3d.position = new THREE.Vector3 waypoints[0].position.x, 0, waypoints[0].position.z
		model
		
	createMesh: (id, name, data) ->
		mesh = materialHelper.createAnimMesh data[0], data[1], name, id: id
		mesh.rotation.y = 90 * (Math.PI / 180)
		mesh
	
	getPathById: (id) ->
		allPaths = db.get 'paths'
		foundPaths = allPaths.where dbId : id
		unless foundPaths.length isnt 0 then throw "Couldnt find path for unit #{name}"
		return foundPaths[0]
		
return Enemies