AnimatedModel = require 'models/animatedModel'
scenegraph = require 'scenegraph'

class Enemy extends AnimatedModel
	
	currentHealth: 1000
	
	maxHealth: 1000
	
	lastDistance: null
	
	waypoints: []
	
	meshCurrentHealth: null
	
	meshMaxHealth: null
	
	meshHealthSize:
		bar:
			width   : 25
			height  : 2.5
			depth   : 0.1
			color   : 0xFF0000
			opacity : 1.0
		border:
			width   : 27
			height  : 4.5
			depth   : 0.1
			color   : 0x000000
			opacity : 0.8
	
	constructor: (@id, @name, @meshBody) ->
		super @id, @name, @meshBody
		@meshMaxHealth = @_createMesh @meshHealthSize.border
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
		width = if percent or percent is 0 then @meshHealthSize.bar.width * percent else @meshHealthSize.bar.width
		newY = (@meshBody.geometry.boundingBox.max.y - @meshBody.geometry.boundingBox.min.y) * 0.8
		newZ = (@meshBody.geometry.boundingBox.max.x - @meshBody.geometry.boundingBox.min.x) * 0.8
		@meshCurrentHealth.position.copy @root.position if @meshCurrentHealth
		@meshCurrentHealth.position.x -= (@meshHealthSize.bar.width - width) / 2
		@meshCurrentHealth.position.y += newY + @meshHealthSize.bar.depth
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
			@meshCurrentHealth = @_createMesh @meshHealthSize.bar, @currentHealth / @maxHealth
			scenegraph.scene.add @meshCurrentHealth
			@_updateHealthBarPosition()

	_createMesh: (size, percent) ->
		width = if percent then size.width * percent else size.width
		geometry = new THREE.CubeGeometry width, size.depth, size.height
		if size.opacity is 1.0
			material = new THREE.MeshPhongMaterial ambient: 0x000000, color: size.color, specular: 0x555555, shininess: 30
		else
			material = new THREE.MeshBasicMaterial color: size.color, opacity: size.opacity, transparent: true
		return new THREE.Mesh geometry, material

return Enemy