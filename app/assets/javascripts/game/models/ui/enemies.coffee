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

	onPacket: (packet) ->
		if packet 
			current = if @get('current') then @get('current') else 0
			if packet.type is PacketType.SERVER_OBJECT_IN
				quantity = if @get('quantity') then @get('quantity') else 0
				# TODO: we need here the info how much enemies are on the field for late joiner
				@createEnemy packet
				@set 
					'current': ++current
					'quantity': ++quantity
			else if packet.type is PacketType.SERVER_OBJECT_OUT
				@set 'current', --current
		return
	
	createEnemy: (opts) ->
		path = @getPathById opts.path
		loadedData = db.data().geometries[opts.name]
		dynObj = @createModel opts, db.data().geometries[opts.name], _.clone path.get 'waypoints'
		scenegraph.addDynObject dynObj, opts.id
		return
			
	createModel: (opts, data, waypoints) ->
		mesh = @createMesh opts.id, opts.name, data
		#mesh.userData.glowing = true
		model = new Enemy opts.id, opts.name, mesh
		model.maxHealth = opts.health
		model.currentHealth = opts.health
		model.maxShield = opts.shield
		model.currentShield = opts.shield
		model.type = opts.utype
		if _.isArray waypoints
			model.waypoints = waypoints
			model.getMainObject().position = new THREE.Vector3 waypoints[0].position.x, 0, waypoints[0].position.z
		model
		
	createMesh: (id, name, data) ->
		if data[0].morphTargets.length isnt 0
			mesh = materialHelper.createAnimMesh data[0], data[1], name, id: id
		else
			mesh = materialHelper.createMesh data[0], data[1], name, id: id
		if _.isObject data[2]
			mesh.scale.x = data[2].scale
			mesh.scale.y = data[2].scale
			mesh.scale.z = data[2].scale
		THREE.GeometryUtils.center mesh.geometry
		mesh.rotation.y = THREE.Math.degToRad -90
		mesh.position.y -= mesh.scale.y * mesh.geometry.boundingBox.min.y
		mesh
	
	getPathById: (id) ->
		allPaths = db.get 'db/paths'
		foundPaths = allPaths.where dbId : id
		unless foundPaths.length isnt 0 then throw "Couldnt find path for unit #{name}"
		return foundPaths[0]
		
return Enemies