AnimatedModel = require 'models/animatedModel'
MuzzleFlash = require 'effects/muzzleFlash'
scenegraph = require 'scenegraph'

class Tower extends AnimatedModel
	
	shotId: 5000 # TODO: this must be dynamic somehow
	
	constructor: (@id, @name, @meshBody) ->
		super @id, @name, @meshBody
		@weapons = []
		@active = false
		@range = 0
		@meshRange = null
		@target = null
	
	update: (delta, now) ->
		if @active
			@_rotateToTarget delta
			super delta, now

	attack: (target, damage) ->
		for weapon in @weapons
			position = @getMainObject().localToWorld new THREE.Vector3 weapon.position.x, weapon.position.y, weapon.position.z
			muzzleFlash = new MuzzleFlash position: position
			@effects.push muzzleFlash
			Weapon = require "models/#{weapon.type.toLowerCase()}"
			laser = new Weapon @shotId++, @, target, damage
			laser.getMainObject().position.copy position
			laser.getMainObject().quaternion.copy @getMainObject().quaternion
			scenegraph.addDynObject laser, laser.id
		return

	showRange: ->
		unless @meshRange
			material = new THREE.MeshBasicMaterial opacity: 0.3, transparent: true
			geometry = new THREE.CircleGeometry @range, @range / 2
			@meshRange = new THREE.Mesh geometry, material
			@meshRange.name = 'range'
			@meshRange.position.y += 1.5
			@meshRange.rotation.x = THREE.Math.degToRad -90
			@getMainObject().add @meshRange
		@meshRange.visible = true

	hideRange: ->
		@meshRange.visible = false if @meshRange

	_rotateToTarget: (delta) ->
		@rotateTo @target.getMainObject().position, delta if @_isValidTarget()

	_isValidTarget: ->
		return if @target and not @target.isDead() and @target.getMainObject().position.distanceTo(@getMainObject().position) <= @range then true else false

return Tower