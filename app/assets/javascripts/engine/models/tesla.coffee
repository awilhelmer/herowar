WeaponModel = require 'models/weapon'
Bolt = require 'models/bolt'

class TeslaModel extends WeaponModel
	
	glowColor: 0xffa500

	createMeshBody: ->
		material = new THREE.MeshBasicMaterial color: @glowColor, transparent: true, opacity: 0.7
		mesh = new Bolt material, 10
		mesh.build()
		console.log 'Created bolt', mesh
		return mesh

	update: (delta, now) ->
		super delta, now
		return unless @target
		@onHit()
		@target = null
		return

	onHit: ->
		super()
		setTimeout =>
			@dispose()
		, 300

return TeslaModel