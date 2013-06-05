AnimatedModel = require 'models/animatedModel'
BlackSmoke = require 'effects/blackSmoke'
Explosion = require 'effects/explosion'
events = require 'events'

class Enemy extends AnimatedModel
	
	showHealth: true
	
	glowing: false

	constructor: (opts, @meshBody) ->
		opts = _.extend {}, opts
		super opts.id, opts.name, @meshBody
		@initialize opts
		
	initialize: (opts) ->
		@setHealth opts.health
		@setShield opts.shield
		@type = opts.type
		@burning = opts.burning
		@explode = opts.explode
		@damageIncoming = 0
		@glowIsActive = false
		@lastDistance = null
		@dead = false
		@waypoints = []
		@effects.push new BlackSmoke @ if @burning
		return

	update: (delta, now) ->
		super delta, now
		if not @isDead()
			return if @waypoints.length is 0
			@_waypointArrivalCheck()
			return if @waypoints.length is 0
			waypoint = @waypoints[0]
			@setAnimation 'run' unless @activeAnimation is 'run'
			@rotateTo waypoint.position, delta
			@move delta
		else if @isDead() and not @activeAnimation
			setTimeout =>
				if @explode
					@dispose()
					return
				for material in @meshBody.material.materials
					opacity = material.opacity - delta
					if opacity > 0
						material.opacity = opacity
					else
						@dispose()
						return
			, 2000
	
	hit: (damage) ->
		@damageIncoming -= damage
		unless @isDead()
			if @currentShield >= damage
				@currentShield -= damage
				@showShield() 
			else
				realDamage = damage - @currentShield
				@currentShield = 0					
				@currentHealth -= realDamage
				@currentHealth = 0 if @currentHealth < 0
			percent = @currentHealth / @maxHealth * 100
			@kill() if @currentHealth is 0
			events.trigger 'unit:damage', @, damage
		return
	
	isDead: ->
		return @currentHealth <= 0
	
	isSoonDead: ->
		return @currentHealth <= 0 or @currentHealth + @currentShield + @maxHealth * 0.1 <= @damageIncoming
	
	kill: ->
		unless @dead
			@dead = true
			@currentShield = 0
			@currentHealth = 0
			@setAnimation 'crdeath', true
			@effects.push new Explosion @ if @explode
		return
	
	getHealthPercentage: ->
		return @currentHealth / @maxHealth * 100
	
	setHealth: (@currentHealth, max) ->
		if max then @maxHealth = max else @maxHealth = @currentHealth
		return
		
	setShield: (@currentShield, max) ->
		if max then @maxShield = max else @maxShield = @currentShield
		return

	showShield: ->
		return if @glowIsActive
		@glowIsActive = true
		@enableGlow()
		setTimeout =>
			@disableGlow()
			@glowIsActive = false
		, 100		
		
	_waypointArrivalCheck: ->
		waypoint = @waypoints[0]
		distance = @getMainObject().position.distanceTo waypoint.position
		if distance < 2 or (lastDistance and lastDistance > distance)
			@_waypointReached waypoint
			lastDistance = null
		else lastDistance = distance
		return

	_waypointReached: (waypoint) ->
		@waypoints.splice 0, 1
		@dispose() if @waypoints.length is 0

return Enemy