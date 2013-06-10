WeaponModel = require 'models/weapon/base'
meshesFactory = require 'factory/meshes'
FireTrail = require 'effects/fireTrail'

class RocketModel extends WeaponModel
	
	glowColor: 0x00a5ff
		
	constructor: (@id, @owner, @target, @damage) ->
		super @id, @owner, @target, @damage
		@isNew = true

	createMeshBody: ->
		return meshesFactory.create @id, 'rocket'

	update: (delta, now) ->
		super delta, now
		if @isNew
			@isNew = false
			@effects.push new FireTrail @
		@dispose() if @effects.length is 0
		return
		
	onHit: ->
		super()
		for effect in @effects
			effect.done = true
			effect.stop()
		@showExplosion()
		return

return RocketModel