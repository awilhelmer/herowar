db = require 'database'

class BlackSmoke

	maxAge: 1.5
	
	constructor: (@container, @position) ->
		data = db.data()
		if data['textures'] and data['textures']['cloud10']
			@texture = db.data()['textures']['cloud10']
		else 
			@texture = THREE.ImageUtils.loadTexture 'assets/images/game/textures/effects/cloud10.png'
		@updateFcts = []
		@sprites = []
		@age2Opacity = @createTweenMidi @maxAge, 0.05 * @maxAge, 0.4 * @maxAge
		@age2Friction = ( =>
			gradient = @createLinearGradient [ {x : 0.00, y: 1.00}, {x : 0.50, y: 1.00}, {x : 0.70, y: 0.90}, {x : 1.00, y: 0.90} ]
			return (age, maxAge) ->
				return gradient age / maxAge
		)();
	
	emit: () ->
		material = new THREE.SpriteMaterial
			map: @texture
			useScreenCoordinates: false
		
		sprite = new THREE.Sprite material
		sprite.position.copy @position
		sprite.position.x	+= (Math.random() - 0.5) * 0.1
		sprite.position.y	+= (Math.random() - 0.5) * 0.0001
		sprite.position.z	+= (Math.random() - 0.5) * 0.1
		@sprites.push sprite
		@container.add sprite
		
		velocity = new THREE.Vector3 0, 1, 0
		velocity.x += (Math.random() - 0.5) * 0.3;
		velocity.y += (Math.random() - 0.5) * 0.3;
		velocity.z += (Math.random() - 0.5) * 0.3;
		velocity.setLength 1.15 + (Math.random() - 0.5) * 0.3
		material.color.setHex 0x010101 * Math.floor(255 * (Math.random() * 0.1 + 0.3))
		sprite.scale.set(0.1,0.1,0.1).multiplyScalar 0.8 + Math.random() * 0.2
		sprite.rotation	= Math.random() * Math.PI * 2
		material.opacity = @age2Opacity 0
		
		birthDate	= Date.now() / 1000
		@updateFcts.push callback = (delta, now) =>
			age	= now - birthDate
			if age >= @maxAge
				sprite.parent.remove sprite
				@sprites.splice @sprites.indexOf(sprite), 1
				@updateFcts.splice @updateFcts.indexOf(callback), 1
				return
			velocity.multiplyScalar @age2Friction age, @maxAge
			sprite.position.add velocity.clone().multiplyScalar delta
			sprite.scale.multiplyScalar 1.005
			material.opacity = @age2Opacity age
		return @
	
	start: (position, rate) ->
		@position = position if position
		rate = 5 unless rate
		lastEmit = 0
		@updateFcts.push @_loopCb = (delta, now) =>
			return if rate is 1 or now - lastEmit < 1 / rate
			lastEmit = now
			@emit()
		return @
		
	stop: ->
		@updateFcts.splice @updateFcts.indexOf(@_loopCb), 1
		return @
	
	update: (delta, now, position) ->
		@position = position if position
		@updateFcts.forEach (updateFct) ->
			updateFct delta, now
		return @

	dispose: ->
		sprite.parent.remove sprite for sprite in @sprites
		@sprites.length = 0

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

return BlackSmoke