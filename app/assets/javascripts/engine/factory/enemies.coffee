meshesFactory = require 'factory/meshes'
scenegraph = require 'scenegraph'
Enemy = require 'models/enemy'
db = require 'database'

enemies =

	create: (opts) ->
		path = @_getPathById opts.path
		dynObj = @_createModel opts, _.clone path.get 'waypoints'
		scenegraph.addDynObject dynObj, opts.id
		return

	_createModel: (opts, waypoints) ->
		model = new Enemy opts.id, opts.name, @_createMesh opts.id, opts.name
		model.setHealth opts.health
		model.setShield opts.shield
		model.type = opts.utype
		if _.isArray waypoints
			model.waypoints = waypoints
			model.getMainObject().position = new THREE.Vector3 waypoints[0].position.x, 0, waypoints[0].position.z
		model
		
	_createMesh: (id, name) ->
		mesh = meshesFactory.create id, name
		mesh.rotation.y = THREE.Math.degToRad -90
		mesh
	
	_getPathById: (id) ->
		allPaths = db.get 'db/paths'
		foundPaths = allPaths.where dbId : id
		unless foundPaths.length isnt 0 then throw "Couldnt find path for unit #{name}"
		return foundPaths[0]

return enemies