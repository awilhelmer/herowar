AnimatedModel = require 'models/scene/animatedModel'
BlackSmoke = require 'effects/blackSmoke'
meshesFactory = require 'factory/meshes'
events = require 'events'

class Enemy extends AnimatedModel
	
	showHealth: true
	
	glowing: false

	constructor: (opts) ->
		opts = _.defaults {}, opts, 
			selectedColor: '#FF0000'
		@meshBody = opts.meshBody = meshesFactory.create opts.id, opts.name
		@initialize opts
		super opts
		@effects.push new BlackSmoke @ if @burning
			
	initialize: (opts) ->
		@setHealth opts.health
		@setShield opts.shield
		@scaleGlow = opts.scaleGlow || 1
		@type = opts.type
		@burning = opts.burning || false
		@explode = opts.explode || false
		@damageIncoming = 0
		@lastDistance = null
		@dead = false
		@meshBody.rotation.set THREE.Math.degToRad(opts.rotation.x), THREE.Math.degToRad(opts.rotation.y), THREE.Math.degToRad(opts.rotation.z) if opts.rotation
		@waypoints = []
		@moveSpeed = opts.moveSpeed
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

	showShield: ->
		@showGlow 100
		return
	
	kill: ->
		unless @dead
			@dead = true
			@currentShield = 0
			@currentHealth = 0
			@setAnimation 'crdeath', true
			@showExplosion 400 if @explode
		return
	
	getHealthPercentage: ->
		return @currentHealth / @maxHealth * 100
	
	setHealth: (@currentHealth, max) ->
		if max then @maxHealth = max else @maxHealth = @currentHealth
		return
		
	setShield: (@currentShield, max) ->
		if max then @maxShield = max else @maxShield = @currentShield
		return
		
	_waypointArrivalCheck: ->
		waypoint = @waypoints[0]
		distance = @getMainObject().position.distanceTo waypoint.position
		if distance < 2 or (@lastDistance and distance > @lastDistance)
			@_waypointReached waypoint
			@lastDistance = null
		else @lastDistance = distance
		return

	_waypointReached: (waypoint) ->
		@waypoints.splice 0, 1
		@dispose() if @waypoints.length is 0

return Enemy