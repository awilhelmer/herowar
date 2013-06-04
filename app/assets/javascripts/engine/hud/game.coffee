UnitDamage = require 'hud/elements/unitDamage'
BaseHUD = require 'hud/baseHud'
events = require 'events'

__damage__ = {}

class GameHUD extends BaseHUD
	
	default: [
		'hud/elements/waveProgress'
		'hud/elements/unitHealthBars'
	]

	bindEvents: ->
		events.on 'unit:damage', @showUnitDamage, @
		super()

	showUnitDamage: (unit, damage) ->
		if _.has __damage__, unit.id
			__damage__[unit.id] += damage
		else 
			__damage__[unit.id] = damage
			setTimeout =>
				@elements.push new UnitDamage @canvas, @view, unit, __damage__[unit.id]
				delete __damage__[unit.id]
			, 100
		return

return GameHUD