AnimatedModel = require 'models/animatedModel'
scenegraph = require 'scenegraph'

class Enemy extends AnimatedModel
	
	currentHealth: 1000
	
	maxHealth: 1000
	
	showHealth: true
	
	lastDistance: null
	
	waypoints: []

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
	
	hit: (damage) ->
		unless @isDead()
			@currentHealth -= damage
			@currentHealth = 0 if @currentHealth < 0
			if @currentHealth is 0
				@setAnimation 'crdeath', true
	
	isDead: ->
		@currentHealth <= 0
		
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

return Enemy