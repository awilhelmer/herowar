BaseEffect = require 'effects/baseEffect'
scenegraph = require 'scenegraph'

class BaseEmitterEffect extends BaseEffect
	
	maxAge: 1.5
	
	rate: 5
	
	tiles: 1
	
	constructor: (@owner) ->
		super()
		@updateFcts = []
		@sprites = []
		@run = false
	
	update: (delta, now) ->
		now = now / 1000
		@updateFcts.forEach (updateFct) ->
			updateFct delta, now
		return

	dispose: ->
		@stop() if @run
		sprite.parent.remove sprite for sprite in @sprites
		@sprites.length = 0
		super()

	start: ->
		unless @run
			@run = true
			lastEmit = 0
			@updateFcts.push @_loopCb = (delta, now) =>
				return if @rate is 1 or now - lastEmit < 1 / @rate
				lastEmit = now
				@emit()
		return
		
	stop: ->
		if @run
			@run = false
			@updateFcts.splice @updateFcts.indexOf(@_loopCb), 1 if @_loopCb
		return

	emit: ->
		material = @createMaterial()
		sprite = @createSprite material	
		velocity = @createVelocity()			
		@spawn material, sprite, velocity
		return
		
	spawn: (material, sprite, velocity) ->
		birthDate	= Date.now() / 1000
		@updateFcts.push callback = (delta, now) =>
			age	= now - birthDate
			if age >= @maxAge
				sprite.parent.remove sprite
				@sprites.splice @sprites.indexOf(sprite), 1
				@updateFcts.splice @updateFcts.indexOf(callback), 1
				return
			@updateSpawn velocity, sprite, material, delta, age, @maxAge
		return

	updateSpawn: (velocity, sprite, material, delta, age, maxAge) ->
		@updateVelocity velocity, age, @maxAge
		@updateSprite sprite, velocity, delta
		@updateMaterial material, age, @maxAge
		return

	createMaterial: ->
		material = new THREE.SpriteMaterial
			map: @getTexture()
			useScreenCoordinates: false
		if @tiles isnt 1
			material.uvScale.set 1, 1 / @tiles
			material.uvOffset.set 0, @age2uvOffset 0, @maxAge
		material.color.setHex 0x010101 * Math.floor(255 * (Math.random() * 0.1 + 0.3))
		material.opacity = @age2Opacity 0, @maxAge
		return material
	
	updateMaterial: (material, age, maxAge) ->
		material.opacity = @age2Opacity age, maxAge
		material.uvOffset.set 0, @age2uvOffset age, maxAge if @tiles isnt 1
		return

	createSprite: (material) ->
		sprite = new THREE.Sprite material
		@setSpritePosition sprite
		@setSpriteScale sprite
		@setSpriteRotation sprite		
		@sprites.push sprite
		scenegraph.scene().add sprite
		return sprite

	setSpritePosition: (sprite) ->
		sprite.position.copy @getPosition()
		sprite.position.y	+= 10
		sprite.rotation.x = THREE.Math.degToRad -90
		sprite.position.x	+= (Math.random() - 0.5) * 0.1
		sprite.position.y	+= (Math.random() - 0.5) * 0.0001
		sprite.position.z	+= (Math.random() - 0.5) * 0.1
		return

	setSpriteScale: (sprite) ->
		sprite.scale.set(0.1,0.1,0.1).multiplyScalar 0.8 + Math.random() * 0.2
		return

	setSpriteRotation: (sprite) ->
		sprite.rotation	= Math.random() * Math.PI * 2
		return

	updateSprite: (sprite, velocity, delta) ->
		sprite.position.add velocity.clone().multiplyScalar delta
		sprite.scale.multiplyScalar 1.005
		return

	createVelocity: ->
		velocity = new THREE.Vector3 0, 1, 0
		velocity.x += (Math.random() - 0.5) * 0.3
		velocity.y += (Math.random() - 0.5) * 0.3
		velocity.z += (Math.random() - 0.5) * 0.3
		velocity.setLength 1.15 + (Math.random() - 0.5) * 0.3
		return velocity

	updateVelocity: (velocity, age, maxAge) ->
		velocity.multiplyScalar @age2Friction age, maxAge
		return

	createTweenMidi: (maxAge, attackTime, releaseTime) ->
		return (age) ->
			if age < attackTime
				return age / attackTime
			else if age < maxAge - releaseTime
				return 1
			return (maxAge - age) / releaseTime

	createLinearGradient: (keyPoints) ->
		return (x) ->
			break for i in [0..keyPoints.length-1] when x <= keyPoints[i].x
			return keyPoints[0].y if i is 0
			previous = keyPoints[i - 1]
			next = keyPoints[i]
			ratio	= (x - previous.x) / (next.x - previous.x)
			return previous.y + ratio * (next.y - previous.y)

	age2Opacity: (age, maxAge) -> 
		tween = @createTweenMidi maxAge, 0.05 * maxAge, 0.4 * maxAge
		return tween age

	age2Friction: (age, maxAge) ->
		gradient = @createLinearGradient [ {x : 0.00, y: 1.00}, {x : 0.50, y: 1.00}, {x : 0.70, y: 0.90}, {x : 1.00, y: 0.90} ]
		return gradient age / maxAge

	age2uvOffset: (age, maxAge) ->
		imageIdx = Math.floor age / maxAge * @tiles
		#console.log 'age2uvOffset', (age / maxAge), (Math.floor age / maxAge * @tiles), (1 - imageIdx * 1 / @tiles)
		return 1 - imageIdx * 1 / @tiles

	getPosition: ->
		boundingBox = @owner.meshBody.geometry.boundingBox
		position = @owner.getMainObject().position.clone()
		position.y += (boundingBox.max.y - boundingBox.min.y) * @owner.meshBody.scale.y / 2
		return position

return BaseEmitterEffect