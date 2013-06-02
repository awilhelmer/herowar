AnimatedModel = require 'models/animatedModel'
BlackSmoke = require 'effects/blackSmoke'
scenegraph = require 'scenegraph'

class Enemy extends AnimatedModel
	
	currentHealth: 1000
	
	maxHealth: 1000
	
	showHealth: true
	
	lastDistance: null
	
	waypoints: []

	update: (delta) ->
		super delta
		@blackSmoke.update delta, Date.now() / 1000, @_getBlackSmokePosition() if @blackSmoke
		if not @isDead()
			return if @waypoints.length is 0
			@_waypointArrivalCheck()
			return if @waypoints.length is 0
			waypoint = @waypoints[0]
			@setAnimation 'run' unless @activeAnimation is 'run'
			@rotateTo waypoint.position, delta
			@move delta
		else if @isDead() and not @activeAnimation
			for material in @meshBody.material.materials
				opacity = material.opacity - delta
				if opacity > 0
					material.opacity = opacity
				else
					@blackSmoke.dispose() if @blackSmoke
					@dispose()
					break
	
	hit: (damage) ->
		unless @isDead()
			@currentHealth -= damage
			@currentHealth = 0 if @currentHealth < 0
			percent = @currentHealth / @maxHealth * 100
			@_createBlackSmoke() if percent <= 50 and not @blackSmoke and @meshBody.name is 'Spaceship-1-v1'
			if @currentHealth is 0
				@setAnimation 'crdeath', true 
				@blackSmoke.stop() if @blackSmoke
		return
	
	isDead: ->
		@currentHealth <= 0
		
	_waypointArrivalCheck: ->
		waypoint = @waypoints[0]
		distance = @getMainObject().position.distanceTo waypoint.position
		if distance < 2 or (lastDistance and lastDistance > distance)
			@_waypointReached waypoint
			lastDistance = null
		else lastDistance = distance
		return

	_waypointReached: (waypoint) ->
		#console.log "Enemy #{@name}-#{@id} reached #{waypoint.name}"
		@waypoints.splice 0, 1
		@dispose() if @waypoints.length is 0

	_createBlackSmoke: ->
		@blackSmoke = new BlackSmoke scenegraph.scene()
		@blackSmoke.start @_getBlackSmokePosition()

	_getBlackSmokePosition: ->
		boundingBox = @meshBody.geometry.boundingBox
		position = @root.main.position.clone()
		position.y += (boundingBox.max.y - boundingBox.min.y) * @meshBody.scale.y / 2
		return position

return Enemy