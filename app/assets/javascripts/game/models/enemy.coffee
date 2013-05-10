BaseModel = require 'models/basemodel'

class Enemy extends BaseModel
	
	waypoints: []
	
	constructor: (@object3d) ->
		@object3d.useQuaternion = true
		@target = new THREE.Object3D()
	
	update: (delta) ->
		@move delta
	
	move: (delta) ->
		return unless @waypoints.length isnt 0
		waypoint = @waypoints[0]
		console.log 'Moving', waypoint.position.x - @object3d.position.x, waypoint.position.z - @object3d.position.z 
		@rotateTo waypoint.position
		@object3d.translateZ delta * 10

	rotateTo: (position) ->
		#console.log 'Look at', position
		#target = new THREE.Vector3 position.x, position.y, position.z
		#up = new THREE.Vector3 0, 1, 0
		#orientation = new THREE.Matrix4()
		#orientation.lookAt target, @object3d.position, up
		#@object3d.applyMatrix orientation
		@object3d.lookAt new THREE.Vector3 position.x, 0, position.z
		#destRotation = new THREE.Quaternion position.x, position.y, position.z, 1
		#resultQuaternion = new THREE.Quaternion()
		#THREE.Quaternion.slerp @object3d.quaternion, destRotation, resultQuaternion, 0.07
		#@object3d.quaternion = resultQuaternion
		#@object3d.quaternion.normalize()
		#@object3d.quaternion.slerp destRotation, 0.3
		#@object3d.quaternion.normalize()
	
return Enemy