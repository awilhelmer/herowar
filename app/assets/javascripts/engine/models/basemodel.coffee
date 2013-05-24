class BaseModel

	rotationMultipler: null

	moveSpeed: 30

	constructor: (@root) ->
	
	update: (delta) ->
		
	dispose: ->
		scenegraph = require 'scenegraph'
		scenegraph.removeDynObject @id

	rotateTo: (position, delta) ->
		target = new THREE.Vector3 position.x, 0, position.z
		m = new THREE.Matrix4()
		m.lookAt target, @root.position, @root.up
		dq = new THREE.Quaternion()
		dq.setFromRotationMatrix m
		multipler = if @rotationMultipler then @rotationMultipler else @moveSpeed / 10
		@root.quaternion.slerp dq, delta * multipler

return BaseModel 