BaseModel = require 'models/basemodel'
events = require 'events'

class Enemy extends BaseModel
	
	id: null
	
	name: null
	
	meshBody: null
	
	activeAnimation = null
	
	waypoints: []
	
	scale: 1
	animationFPS: 6
	
	first: true
	
	constructor: (@id, @name, @meshBody) ->
		# Enable shadows
		@meshBody.castShadow = true
		@meshBody.receiveShadow = true
		# Create Object3D
		obj = new THREE.Object3D()
		obj.name = @name
		obj.useQuaternion = true
		obj.add @meshBody
		@setAnimation @meshBody.geometry.firstAnimation
		
		super obj
	
	update: (delta) ->
		@animate delta
		@move delta
	
	move: (delta) ->
		return if @waypoints.length is 0
		@_waypointArrivalCheck()
		return if @waypoints.length is 0
		waypoint = @waypoints[0]
		@setAnimation 'run' unless @activeAnimation is 'run'
		@_rotateTo waypoint.position
		@object3d.translateZ delta * 20
	
	animate: (delta) ->
		@meshBody.updateAnimation 1000 * delta  if @meshBody and @activeAnimation
	
	setAnimation: (name) ->
		console.log "Enemy #{@name}-#{@id} set animation to #{name}"
		if @meshBody
			@meshBody.playAnimation name, @animationFPS 
			@meshBody.baseDuration = @meshBody.duration
		@activeAnimation = name

	_rotateTo: (position) ->
		target = new THREE.Vector3 position.x, 0, position.z
		m = new THREE.Matrix4()
		m.lookAt target, @object3d.position, @object3d.up
		dq = new THREE.Quaternion()
		dq.setFromRotationMatrix m
		@object3d.quaternion.slerp dq, 0.07
	
	_waypointArrivalCheck: ->
		waypoint = @waypoints[0]
		@_waypointReached waypoint if Math.abs(waypoint.position.x - @object3d.position.x) < 1 and Math.abs(waypoint.position.z - @object3d.position.z) < 1

	_waypointReached: (waypoint) ->
		console.log "Enemy #{@name}-#{@id} reached #{waypoint.name}"
		@waypoints.splice 0, 1
		events.trigger 'remove:dynamicObject', @id if @waypoints.length is 0

return Enemy