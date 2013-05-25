AnimatedModel = require 'models/animatedModel'
scenegraph = require 'scenegraph'

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
		scenegraph.scene.add @meshMaxHealth
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
		@root.translateZ delta * @moveSpeed
		@_updateHealthBarPosition()
	
	hit: (damage) ->
		unless @isDead()
			@currentHealth -= damage
			@currentHealth = 0 if @currentHealth < 0
			@_updateCurrentHealthMesh()
			if @currentHealth is 0
				@setAnimation 'crdeath', true
				scenegraph.scene.remove @meshMaxHealth
			
	
	isDead: ->
		@currentHealth <= 0
	
	_updateHealthBarPosition: ->
		percent = @currentHealth / @maxHealth
		width = if percent or percent is 0 then @meshHealthBarSize.width * percent else @meshHealthBarSize.width
		newY = @meshBody.geometry.boundingBox.max.y - @meshBody.geometry.boundingBox.min.y
		newZ = @meshBody.geometry.boundingBox.max.z - @meshBody.geometry.boundingBox.min.z
		@meshCurrentHealth.position.copy @root.position if @meshCurrentHealth
		@meshCurrentHealth.position.x += (@meshHealthBarSize.width - width) / 2
		@meshCurrentHealth.position.y += newY + 1
		@meshCurrentHealth.position.z -= newZ
		@meshMaxHealth.position.copy @root.position if @meshMaxHealth
		@meshMaxHealth.position.y += newY
		@meshMaxHealth.position.z -= newZ
		
	_waypointArrivalCheck: ->
		waypoint = @waypoints[0]
		distance = @root.position.distanceTo waypoint.position
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
			scenegraph.scene.remove @meshCurrentHealth
			@meshCurrentHealth = null
		unless @currentHealth is 0
			@meshCurrentHealth = @_createMesh 0xFF0000, @currentHealth / @maxHealth
			scenegraph.scene.add @meshCurrentHealth
			@_updateHealthBarPosition()

	_createMesh: (color, percent) ->
		width = if percent then @meshHealthBarSize.width * percent else @meshHealthBarSize.width
		geometry = new THREE.CubeGeometry width, @meshHealthBarSize.height, @meshHealthBarSize.depth
		material = new THREE.MeshBasicMaterial color: color, opacity: 0.5
		mesh = new THREE.Mesh geometry, material
		mesh.position.x -= @meshBody.geometry.boundingBox.max.x * 1.2
		mesh.position.y = @meshBody.geometry.boundingBox.max.y * 1.2
		mesh.position.z += (@meshHealthBarSize.width - width) / 2 if percent
		mesh

return Enemy