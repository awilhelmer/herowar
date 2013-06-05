BaseEffect = require 'effects/baseEffect'
scenegraph = require 'scenegraph'
db = require 'database'

class MuzzleFlash extends BaseEffect
	
	maxAge: 200
	
	constructor: (@opts) ->
		data = db.data()
		if data['textures'] and data['textures']['particle001']
			@texture = db.data()['textures']['particle001']
		else 
			@texture = THREE.ImageUtils.loadTexture 'assets/images/game/textures/effects/particle001.png'
		@birthDate	= Date.now()
		@_createParticle()
		super()
		
	update: (delta, now) ->
		age	= now - @birthDate
		if age < @maxAge
			opacity = 1 - age / @maxAge
			@sprite.position = @opts.target.localToWorld @opts.origin.clone()
			@material.opacity = opacity
		else
			@active = false
			scenegraph.scene().remove @sprite
		return
	
	_createParticle: ->
		@material = new THREE.SpriteMaterial
			color: 0xffa500
			useScreenCoordinates : false
			map: @texture
			blending: THREE.AdditiveBlending
			transparent: true
		@sprite = new THREE.Sprite @material
		@sprite.name = 'MuzzleFlash'
		@sprite.position = @opts.position
		@sprite.scale.set 0.075, 0.075, 0.075
		scenegraph.scene().add @sprite
		return
	
return MuzzleFlash