AnimatedModel = require 'models/animatedModel'

class Enemy extends AnimatedModel
	
	currentHealth: 1000
	
	maxHealth: 1000
	
	lastDistance: null
	
	waypoints: []
	
	meshCurrentHealth: null
	
	meshMaxHealth: null
	
	meshHealthBarSize:
		width: 15
		height: 1.5
		depth: 1
	
	constructor: (@id, @name, @meshBody) ->
		super @id, @name, @meshBody
		@meshMaxHealth = @_createMesh 0x808080
		@object3d.add @meshMaxHealth
		@_updateCurrentHealthMesh()
		
	update: (delta) ->
		super delta
		unless @isDead()
			@move delta
		if @isDead() and not @activeAnimation
			opacity = @meshBody.material.materials[0].opacity - delta
			if opacity > 0
				@meshBody.material.materials[0].opacity = opacity
			else
				@dispose()
	
	move: (delta) ->
		return if @waypoints.length is 0
		@_waypointArrivalCheck()
		return if @waypoints.length is 0
		waypoint = @waypoints[0]
		@setAnimation 'run' unless @activeAnimation is 'run'
		@rotateTo waypoint.position, delta
		@object3d.translateZ delta * @moveSpeed
	
	hit: (damage) ->
		unless @isDead()
			@currentHealth -= damage
			@currentHealth = 0 if @currentHealth < 0
			@_updateCurrentHealthMesh()
			if @currentHealth is 0
				@setAnimation 'crdeath', true
				@object3d.remove @meshMaxHealth
			
	
	isDead: ->
		@currentHealth <= 0
	
	_waypointArrivalCheck: ->
		waypoint = @waypoints[0]
		distance = @object3d.position.distanceTo waypoint.position
		if distance < 2 or (lastDistance and lastDistance > distance)
			@_waypointReached waypoint
			lastDistance = null
		else
			lastDistance = distance

	_waypointReached: (waypoint) ->
		#console.log "Enemy #{@name}-#{@id} reached #{waypoint.name}"
		@waypoints.splice 0, 1
		@dispose() if @waypoints.length is 0

	_updateCurrentHealthMesh: ->
		if @meshCurrentHealth
			@object3d.remove @meshCurrentHealth
			@meshCurrentHealth = null
		unless @currentHealth is 0
			@meshCurrentHealth = @_createMesh 0xFF0000, @currentHealth / @maxHealth
			@meshCurrentHealth.position.y += 1
			@object3d.add @meshCurrentHealth

	_createMesh: (color, percent) ->
		width = if percent then @meshHealthBarSize.width * percent else @meshHealthBarSize.width
		geometry = new THREE.CubeGeometry @meshHealthBarSize.height, @meshHealthBarSize.depth, width
		material = new THREE.MeshBasicMaterial color: color, opacity: 0.5
		mesh = new THREE.Mesh geometry, material
		mesh.position.x -= @meshBody.geometry.boundingBox.max.x * 1.2
		mesh.position.y = @meshBody.geometry.boundingBox.max.y * 1.2
		mesh.position.z += (@meshHealthBarSize.width - width) / 2 if percent
		mesh

return Enemy