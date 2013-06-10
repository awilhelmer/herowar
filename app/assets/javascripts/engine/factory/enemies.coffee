scenegraph = require 'scenegraph'
Enemy = require 'models/enemy'
db = require 'database'

enemies =

	create: (opts) ->
		dynObj = @_createModel opts, @_getWaypoints opts.path
		scenegraph.addDynObject dynObj, opts.id
		return dynObj

	_createModel: (opts, waypoints) ->
		model = new Enemy opts
		if _.isArray waypoints
			model.waypoints = waypoints
			model.getMainObject().position = new THREE.Vector3 waypoints[0].position.x, 0, waypoints[0].position.z if waypoints.length > 0
		return model
	
	_getWaypoints: (id) ->
		path = @_getPathById id
		return _.clone path.get 'waypoints' if path
		return []
	
	_getPathById: (id) ->
		allPaths = db.get 'db/paths'
		foundPaths = allPaths.where dbId : id
		return foundPaths[0] unless foundPaths.length is 0

return enemies