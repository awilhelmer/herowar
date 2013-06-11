AnimatedModel = require 'models/scene/animatedModel'
MuzzleFlash = require 'effects/muzzleFlash'
meshesFactory = require 'factory/meshes'
scenegraph = require 'scenegraph'

class Tower extends AnimatedModel
	
	shotId: 5000 # TODO: this must be dynamic somehow
	
	constructor: (opts) ->
		opts = _.defaults {}, opts, 
			active   : false
			range    : 0
			weapons  : []
			position : undefined
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
			Weapon = require "models/weapon/#{weapon.type.toLowerCase()}"
			weaponObj = new Weapon @shotId++, @, target, currentDamage
			weaponObj.getMainObject().position.copy position
			weaponObj.getMainObject().quaternion.copy @getMainObject().quaternion
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
		@rotateTo @target.getMainObject().position, delta if @_isValidTarget()

	_isValidTarget: ->
		return if @target and not @target.isDead() and @target.getMainObject().position.distanceTo(@getMainObject().position) <= @range then true else false

return Tower