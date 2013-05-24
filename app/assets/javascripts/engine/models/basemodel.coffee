class BaseModel

	constructor: (@object3d) ->
	
	update: (delta) ->
		
	dispose: ->
		scenegraph = require 'scenegraph'
		scenegraph.removeDynObject @id

	rotateTo: (position, delta) ->
		target = new THREE.Vector3 position.x, 0, position.z
		m = new THREE.Matrix4()
		m.lookAt target, @object3d.position, @object3d.up
		dq = new THREE.Quaternion()
		dq.setFromRotationMatrix m
		@object3d.quaternion.slerp dq, delta * 2		

return BaseModel 