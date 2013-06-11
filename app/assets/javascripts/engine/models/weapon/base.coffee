AnimatedModel = require 'models/scene/animatedModel'

class BaseWeapon extends AnimatedModel
	
	rotationMultipler: 50
	
	moveSpeed: 150
	
	distanceToDispose: 10

	constructor: (@id, @owner, @target, @damage) ->
		opts = 
			id: @id
			owner: @owner
			target: @target
			damge: @damage
		@meshBody = opts.meshBody = @createMeshBody()
		super opts
		@target.damageIncoming += @damage

	createMeshBody: ->
		geometry = new THREE.CubeGeometry 1, 1, 8.5
		material = new THREE.MeshBasicMaterial color: @glowColor, transparent: true, opacity: 0.7
		mesh = new THREE.Mesh geometry, material
		mesh.userData.glowing = true
		return mesh

	update: (delta, now) ->
		super delta, now
		return unless @target
		targetPosition = @target.getMainObject().position
		distance = @getMainObject().position.distanceTo targetPosition
		if distance > @distanceToDispose
			@rotateTo targetPosition, delta
			@move delta
		else
			@onHit()
			@target = null
		return

	onHit: ->
		@target.hit @damage
		return
		
return BaseWeapon