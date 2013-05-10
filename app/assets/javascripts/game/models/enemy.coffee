BaseModel = require 'models/basemodel'

class Enemy extends BaseModel
	
	waypoints: []
	
	update: (delta) ->
		@move delta
	
	move: (delta) ->
		return unless @waypoints.length isnt 0
		waypoint = @waypoints[0]
		@rotateTo waypoint.position
		@object3d.translateZ delta * 10

	rotateTo: (position) ->
		console.log 'Look at', position
		destRotation = new THREE.Quaternion position.x, position.y, position.z, 1
		#resultQuaternion = new THREE.Quaternion()
		#THREE.Quaternion.slerp @object3d.quaternion, destRotation, resultQuaternion, 0.07
		#@object3d.quaternion = resultQuaternion
		#@object3d.quaternion.normalize()
		@object3d.quaternion.slerp destRotation, 0.3
		@object3d.quaternion.normalize()
	
return Enemy