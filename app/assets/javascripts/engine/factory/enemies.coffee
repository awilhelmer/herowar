scenegraph = require 'scenegraph'
Enemy = require 'models/scene/enemy'
db = require 'database'

enemies =

	create: (opts) ->
		#console.log 'Create enemy', opts
		dynObj = @_createModel opts
		scenegraph.addDynObject dynObj, opts.id
		return dynObj

	_createModel: (opts) ->
		model = new Enemy opts
		model.waypoints = @_getWaypoints opts
		model.getMainObject().position.set opts.position.x, 0, opts.position.z if opts.position
		return model
	
	_getWaypoints: (opts) ->
		currentWaypoint = opts.waypoint || -1
		currentPath = @_getPath opts
		allWaypoints = db.get 'db/waypoints'
		waypointModels = allWaypoints.where path : currentPath.id
		if waypointModels?.length > 0
			waypoints = []
			for waypoint in waypointModels
				if waypoint.get('dbId') is currentWaypoint or currentWaypoint is -1
					waypoints.push waypoint.toJSON()
					currentWaypoint = -1 if currentWaypoint isnt -1
			return waypoints
		return []
	
	_getPath: (opts) ->
		allPaths = db.get 'db/paths'
		foundPaths = allPaths.where dbId : opts.path
		return foundPaths[0]

return enemies