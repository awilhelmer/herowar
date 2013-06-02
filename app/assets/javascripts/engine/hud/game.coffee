UnitDamage = require 'hud/elements/unitDamage'
BaseHUD = require 'hud/baseHud'
events = require 'events'

class GameHUD extends BaseHUD
	
	default: [
		'hud/elements/wave'
		'hud/elements/unitHealthBars'
	]

	bindEvents: ->
		events.on 'unit:damage', @showUnitDamage, @
		super()

	showUnitDamage: (unit, damage) ->
		@elements.push new UnitDamage @canvas, @view, unit, damage
		return

return GameHUD