BaseEmitterEffect = require 'effects/baseEmitterEffect'
textureUtils = require 'util/textureUtils'
scenegraph = require 'scenegraph'
db = require 'database'

__texture__ = null

class Fire extends BaseEmitterEffect
	
	maxAge: 0.75
	
	tiles: 25
	
	scale: new THREE.Vector3 0.15, 0.15, 0.15
	
	scaleMultiplier: 1.010
	
	runOnce: false
		
	constructor: (@owner) ->
		super @owner
		__texture__ = @getTexture() unless __texture__
		@start()
	
	update: (delta, now) ->
		super delta, now
		@active = false if @done and @updateFcts.length is 0
		return
	
	emit: ->
		super()
		if @runOnce
			@stop()
			@done = true
		return
	
	createMaterial: ->
		material = new THREE.SpriteMaterial
			map: __texture__
			useScreenCoordinates: false
			transparent: true
			blending: THREE.AdditiveBlending
		if @tiles isnt 1
			material.uvScale.set 1, 1 / @tiles
			material.uvOffset.set 0, @age2uvOffset 0, @maxAge
		material.opacity = @age2Opacity 0, @maxAge
		return material

	age2Opacity: (age, maxAge) -> 
		tween = @createTweenMidi 1, 0.1, 0.5
		return tween age / maxAge
	
	setSpriteScale: (sprite) ->
		sprite.scale.copy @scale
		return

	createVelocity: ->
		@initVelocity = new THREE.Vector3 0, 0, 0 unless @initVelocity
		speed = @initVelocity.length()
		velocity = @initVelocity.clone().normalize()
		velocity.x += (Math.random() - 0.5) * 0.0
		velocity.y += (Math.random() - 0.5) * 0.2
		velocity.z += (Math.random() - 0.5) * 0.0
		velocity.setLength speed
		return velocity

	updateSprite: (sprite, velocity, delta) ->
		sprite.position.add velocity.clone().multiplyScalar delta
		sprite.scale.multiplyScalar @scaleMultiplier
		return

	getTexture: ->
		return textureUtils.createFromImage
			image: db.data().images.explosion
			alpha: true

return Fire