AnimatedModel = require 'models/animatedModel'

class Enemy extends AnimatedModel
	
	moveSpeed: 20
	
	waypoints: []
	
	update: (delta) ->
		@move delta
		super delta
	
	move: (delta) ->
		return if @waypoints.length is 0
		@_waypointArrivalCheck()
		return if @waypoints.length is 0
		waypoint = @waypoints[0]
		@setAnimation 'run' unless @activeAnimation is 'run'
		@rotateTo waypoint.position, delta
		@object3d.translateZ delta * @moveSpeed
		
	_waypointArrivalCheck: ->
		waypoint = @waypoints[0]
		distance = @object3d.position.distanceTo waypoint.position
		#console.log "Distance #{distance}"
		@_waypointReached waypoint if distance < 2

	_waypointReached: (waypoint) ->
		console.log "Enemy #{@name}-#{@id} reached #{waypoint.name}"
		@waypoints.splice 0, 1
		@dispose() if @waypoints.length is 0

return Enemy