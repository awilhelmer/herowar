WeaponModel = require 'models/weapon/base'

class LaserModel extends WeaponModel
	
	glowColor: 0xffa500

	onHit: ->
		super()
		@dispose()

return LaserModel