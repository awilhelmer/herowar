BaseModel = require 'models/basemodel'
scenegraph = require 'scenegraph'

class AnimatedModel extends BaseModel

	id: null
	
	name: null
	
	meshBody: null
	
	activeAnimation = null
	
	animationFPS: 6	

	constructor: (@id, @name, @meshBody) ->
		# Enable shadows
		@meshBody.castShadow = true
		@meshBody.receiveShadow = true
		# Create Object3D
		obj = new THREE.Object3D()
		obj.name = @name
		obj.useQuaternion = true
		obj.add @meshBody
		@setAnimation @meshBody.geometry.firstAnimation if @meshBody.geometry.firstAnimation
		super obj

	update: (delta) ->
		@animate delta

	animate: (delta) ->
		@meshBody.updateAnimation 1000 * delta	if @meshBody and @activeAnimation
	
	setAnimation: (name) ->
		console.log "Model #{@name}-#{@id} set animation to #{name}"
		if @meshBody
			@meshBody.playAnimation name, @animationFPS 
			@meshBody.baseDuration = @meshBody.duration
		@activeAnimation = name

	dispose: ->
		scenegraph.removeDynObject @id

	rotateTo: (position, delta) ->
		target = new THREE.Vector3 position.x, 0, position.z
		m = new THREE.Matrix4()
		m.lookAt target, @object3d.position, @object3d.up
		dq = new THREE.Quaternion()
		dq.setFromRotationMatrix m
		@object3d.quaternion.slerp dq, delta * 2

return AnimatedModel