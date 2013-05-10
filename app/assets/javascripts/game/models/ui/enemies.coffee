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
			@createEnemy packet.id, packet.name, packet.position
	
	createEnemy: (id, name, position) ->
		loadedData = db.data().geometries[name]
		mesh = materialHelper.createMesh loadedData[0], loadedData[1], name, id: id
		obj = new THREE.Object3D()
		obj.name = mesh.name
		#obj.position = position
		obj.add mesh
		dynObj = new Enemy obj
		console.log 'Create Enemy from: ', id, name, obj
		events.trigger 'add:dynamicObject', id, dynObj
		
return Enemies