AnimatedModel = require 'models/animatedModel'
BlackSmoke = require 'effects/blackSmoke'
scenegraph = require 'scenegraph'
events = require 'events'

class Enemy extends AnimatedModel
	
	showHealth: true

	constructor: (@id, @name, @meshBody) ->
		super @id, @name, @meshBody
		@damageIncoming = 0
		@maxHealth = 0
		@currentHealth = 0
		@maxShield = 0
		@currentShield = 0
		@type = 0
		@lastDistance = null
		@waypoints = []

	update: (delta, now) ->
		super delta, now
		@blackSmoke.update delta, now / 1000, @_getBlackSmokePosition() if @blackSmoke
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
		@damageIncoming -= damage
		unless @isDead()
			if @currentShield >= damage
				@currentShield -= damage
			else
				realDamage = damage - @currentShield
				@currentShield = 0					
				@currentHealth -= realDamage
				@currentHealth = 0 if @currentHealth < 0
			percent = @currentHealth / @maxHealth * 100
			@_createBlackSmoke() if percent <= 50 and not @blackSmoke and @meshBody.name is 'Spaceship-1-v1'
			if @currentHealth is 0
				@setAnimation 'crdeath', true 
				@blackSmoke.stop() if @blackSmoke
			events.trigger 'unit:damage', @, damage
		return
	
	isDead: ->
		return @currentHealth <= 0
	
	isSoonDead: ->
		return @currentHealth <= 0 or @currentHealth + @currentShield + @maxHealth * 0.1 <= @damageIncoming
	
	kill: ->
		@currentShield = 0
		@currentHealth = 0
		return
		
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