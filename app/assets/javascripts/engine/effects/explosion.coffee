BaseEmitterEffect = require 'effects/baseEmitterEffect'
textureUtils = require 'util/textureUtils'
scenegraph = require 'scenegraph'
db = require 'database'

__texture__ = null

class Explosion extends BaseEmitterEffect
	
	tiles: 25
	
	constructor: (@owner) ->
		super @owner
		__texture__ = @getTexture() unless __texture__
		@removedFromScene = false
	
	update: (delta, now) ->
		unless @done
			@done = true
			@start()
		super delta, now
		@stop() if @run
		return

	updateSpawn: (velocity, sprite, material, delta, age, maxAge) ->
		super velocity, sprite, material, delta, age, maxAge
		if not @removedFromScene and age / maxAge >= 0.3
			@removedFromScene = true
			scenegraph.removeFromScenes @owner
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
		val = tween age / maxAge
		return tween age / maxAge
	
	setSpriteScale: (sprite) ->
		sprite.scale.set(0.18, 0.18, 0.18).multiplyScalar 0.8
		return

	createVelocity: ->
		@initVelocity = new THREE.Vector3 7, 0, 0 unless @initVelocity
		speed = @initVelocity.length()
		velocity = @initVelocity.clone().normalize()
		velocity.x += (Math.random() - 0.5) * 0.0
		velocity.y += (Math.random() - 0.5) * 0.2
		velocity.z += (Math.random() - 0.5) * 0.0
		velocity.setLength speed
		return velocity

	updateSprite: (sprite, velocity, delta) ->
		sprite.position.add velocity.clone().multiplyScalar delta
		sprite.scale.multiplyScalar 1.015
		return

	getTexture: ->
		return textureUtils.createFromImage
			image: db.data().images.explosion
			alpha: true

return Explosion