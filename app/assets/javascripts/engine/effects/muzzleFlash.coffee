BaseEffect = require 'effects/baseEffect'
objectUtils = require 'util/objectUtils'
scenegraph = require 'scenegraph'
db = require 'database'

class MuzzleFlash extends BaseEffect
	
	maxAge: 200
	
	constructor: (@opts) ->
		super()
		@texture = db.data()['textures']['particle001'].clone()
		@texture.needsUpdate = true
		@birthDate	= Date.now()
		@_createParticle()
		
	update: (delta, now) ->
		age	= now - @birthDate
		if age < @maxAge
			opacity = 1 - age / @maxAge
			if @opts.target and @opts.target.position
				newPosition = @opts.target.localToWorld @opts.origin.clone() 
				@sprite.position = newPosition if newPosition
			@material.opacity = opacity
		else
			@dispose()
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
		unless @opts.position
			console.log 'Created muzzle flash with false position', @opts.position
		return

	dispose: ->
			scenegraph.scene().remove @sprite
			objectUtils.dispose @sprite
			super()
	
return MuzzleFlash