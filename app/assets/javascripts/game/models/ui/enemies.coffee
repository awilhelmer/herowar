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
			@createEnemy packet.id, packet.name, packet.path, packet.position
	
	createEnemy: (id, name, pathId, position) ->
		path = @getPathById pathId
		loadedData = db.data().geometries[name]
		mesh = materialHelper.createMesh loadedData[0], loadedData[1], name, id: id
		obj = new THREE.Object3D()
		obj.name = mesh.name
		obj.useQuaternion = true
		#obj.position = position
		obj.add mesh
		dynObj = new Enemy obj
		dynObj.waypoints = path.get 'waypoints'
		events.trigger 'add:dynamicObject', id, dynObj
		
	getPathById: (id) ->
		allPaths = db.get 'paths'
		foundPaths = allPaths.where dbId : id
		unless foundPaths.length isnt 0 then throw "Couldnt find path for unit #{name}"
		return foundPaths[0]
		
return Enemies