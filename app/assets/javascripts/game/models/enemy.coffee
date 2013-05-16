AnimatedModel = require 'models/animatedModel'
events = require 'events'

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
		@_rotateTo waypoint.position, delta
		@object3d.translateZ delta * @moveSpeed
	
	_rotateTo: (position, delta) ->
		target = new THREE.Vector3 position.x, 0, position.z
		m = new THREE.Matrix4()
		m.lookAt target, @object3d.position, @object3d.up
		dq = new THREE.Quaternion()
		dq.setFromRotationMatrix m
		@object3d.quaternion.slerp dq, delta * 2
	
	_waypointArrivalCheck: ->
		waypoint = @waypoints[0]
		distance = @object3d.position.distanceTo waypoint.position
		#console.log "Distance #{distance}"
		@_waypointReached waypoint if distance < 2

	_waypointReached: (waypoint) ->
		console.log "Enemy #{@name}-#{@id} reached #{waypoint.name}"
		@waypoints.splice 0, 1
		events.trigger 'remove:dynamicObject', @id if @waypoints.length is 0

return Enemy