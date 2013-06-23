AnimatedModel = require 'models/scene/animatedModel'
MuzzleFlash = require 'effects/muzzleFlash'
meshesFactory = require 'factory/meshes'
scenegraph = require 'scenegraph'

class Tower extends AnimatedModel
	
	shotId: 50000 # TODO: this must be dynamic somehow
	
	glowColor: 0xffa500
	
	constructor: (opts) ->
		opts = _.defaults {}, opts, 
			active        : false
			range         : 0
			weapons       : []
			position      : undefined
			kills         : 0
			rewardGold    : 0
			selectedColor : '#00FF00'
		@meshBody = opts.meshBody = meshesFactory.create opts.id, opts.name
		super opts
		@weapons = []
		@active = false
		@range = 0
		@meshRange = null
		@target = null
	
	update: (delta, now) ->
		if @active
			@_rotateToTarget delta
			super delta, now

	rotateTo: (position, delta) ->
		if @attributes.rotationSpeed isnt 0 
			super position, delta
		else
			unless @rotationObject
				@rotationObject = new THREE.Object3D()
				@getMainObject().add @rotationObject
			dq = @_getQuaternionRotationFromPosition position
			@rotationObject.quaternion.slerp dq, 1.0

	attack: (target, damage) ->
		return unless @weapons.length isnt 0
		damagePerWeapon = Math.round damage / @weapons.length
		for weapon in @weapons
			if damagePerWeapon > damage
				currentDamage = damage
				damage = 0
			else
				currentDamage = damagePerWeapon
				damage -= damagePerWeapon
			origin = new THREE.Vector3 weapon.position.x, weapon.position.y, weapon.position.z
			position = @getMainObject().localToWorld origin.clone()
			muzzleFlash = new MuzzleFlash target: @getMainObject(), origin: origin, position: position
			@effects.push muzzleFlash
			quaternion = if @rotationObject then @rotationObject.quaternion else @getMainObject().quaternion
			Weapon = require "models/weapon/#{weapon.type.toLowerCase()}"
			weaponObj = new Weapon @shotId++, @, target, currentDamage
			weaponObj.getMainObject().position.copy position
			weaponObj.getMainObject().quaternion.copy quaternion
			scenegraph.addDynObject weaponObj, weaponObj.id
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
		@rotateTo @target.getMainObject().position, delta if @target and not @target.isDead()

return Tower