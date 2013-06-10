BaseWeapon = require 'models/weapon/base'
FireEffect = require 'effects/fire'

class Fire extends BaseWeapon
	
	glowColor: 0xffa500

	constructor: (@id, @owner, @target, @damage) ->
		super @id, @owner, @target, @damage
		@isNew = true

	createMeshBody: ->
		geometry = new THREE.CubeGeometry 1, 1, 1
		material = new THREE.MeshBasicMaterial color: @glowColor, transparent: true, opacity: 0
		mesh = new THREE.Mesh geometry, material
		mesh.visible = false
		return mesh

	update: (delta, now) ->
		super delta, now
		if @isNew
			@isNew = false
			@effects.push new FireEffect @
		@dispose() if @effects.length is 0
		return
		
	onHit: ->
		super()
		for effect in @effects
			effect.done = true
			effect.stop()
		return

return Fire