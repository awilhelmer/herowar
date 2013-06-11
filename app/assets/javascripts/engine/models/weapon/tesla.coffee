WeaponModel = require 'models/weapon/base'
Bolt = require 'models/weapon/bolt'

class TeslaModel extends WeaponModel
	
	glowColor: 0xffa500

	createMeshBody: ->
		material = new THREE.MeshBasicMaterial color: @glowColor, transparent: true, opacity: 0.4
		mesh = new Bolt material, 5 # 10
		mesh.userData.glowing = true
		mesh.scale.x = 0.1
		#mesh.branchPoint.lookAt @target.getMainObject()
		mesh.build()
		#console.log 'Created bolt', mesh
		return mesh

	update: (delta, now) ->
		super delta, now
		return unless @target
		@onHit()
		@target = null
		return

	onHit: ->
		super()
		@owner.showGlow 200
		setTimeout =>
			@dispose()
		, 200

return TeslaModel