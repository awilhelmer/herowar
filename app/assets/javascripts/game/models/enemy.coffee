BaseModel = require 'models/basemodel'
events = require 'events'

class Enemy extends BaseModel
	
	id: null
	
	name: null
	
	meshBody: null
	
	waypoints: []
	
	constructor: (@id, @name, @meshBody) ->
		obj = new THREE.Object3D()
		obj.name = @name
		obj.useQuaternion = true
		obj.add @meshBody
		super obj
	
	update: (delta) ->
		@move delta
	
	move: (delta) ->
		return if @waypoints.length is 0
		@waypointArrivalCheck()
		return if @waypoints.length is 0
		waypoint = @waypoints[0]
		@rotateTo waypoint.position
		@object3d.translateZ delta * 20

	rotateTo: (position) ->
		@object3d.lookAt new THREE.Vector3 position.x, 0, position.z
	
	waypointArrivalCheck: ->
		waypoint = @waypoints[0]
		@waypointReached waypoint if Math.abs(waypoint.position.x - @object3d.position.x) < 1 and Math.abs(waypoint.position.z - @object3d.position.z) < 1

	waypointReached: (waypoint) ->
		console.log "Enemy #{@name}-#{@id} reached #{waypoint.name}"
		@waypoints.splice 0, 1
		events.trigger 'remove:dynamicObject', @id if @waypoints.length is 0

return Enemy