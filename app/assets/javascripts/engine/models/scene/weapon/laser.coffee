WeaponModel = require 'models/scene/weapon/weapon'

class LaserModel extends WeaponModel
	
	glowColor: 0xffa500

	onHit: ->
		super()
		@dispose()

return LaserModel