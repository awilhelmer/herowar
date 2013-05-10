BaseModel = require 'models/basemodel'

class Enemy extends BaseModel
	
	waypoints: []
	
	constructor: (@object3d) ->
		@object3d.useQuaternion = true
		@target = new THREE.Object3D()
	
	update: (delta) ->
		@move delta
	
	move: (delta) ->
		return if @waypoints.length is 0
		waypoint = @waypoints[0]
		if Math.abs(waypoint.position.x - @object3d.position.x) < 1 and Math.abs(waypoint.position.z - @object3d.position.z) < 1
			console.log "Reached #{waypoint.name}"
			@waypoints.splice 0, 1
			return if @waypoints.length is 0
			waypoint = @waypoints[0]
		#console.log 'Moving', waypoint.position.x - @object3d.position.x, waypoint.position.z - @object3d.position.z 
		@rotateTo waypoint.position
		@object3d.translateZ delta * 10

	rotateTo: (position) ->
		@object3d.lookAt new THREE.Vector3 position.x, 0, position.z
	
return Enemy