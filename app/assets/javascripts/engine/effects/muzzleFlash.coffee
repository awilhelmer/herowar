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
		opacity = 1 - age / @maxAge
		if age < @maxAge
			@material.opacity = opacity
		else
			@active = false
			scenegraph.scene().remove @obj
		return
	
	_createParticle: ->
		@material = new THREE.SpriteMaterial
			color: 0xffa500
			useScreenCoordinates : false
			map: @texture
			blending: THREE.AdditiveBlending
			transparent: true
		sprite = new THREE.Sprite @material
		sprite.position = @opts.position
		sprite.scale.set 0.075, 0.075, 0.075
		@obj = new THREE.Object3D()
		@obj.name = 'MuzzleFlash'
		@obj.add sprite
		scenegraph.scene().add @obj
		return
	
return MuzzleFlash