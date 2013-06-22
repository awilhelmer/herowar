UnitDamage = require 'hud/elements/unitDamage'
BaseHUD = require 'hud/baseHud'
events = require 'events'
db = require 'database'

__damage__ = {}

class GameHUD extends BaseHUD
	
	default: [
		'hud/elements/towerInfo'
		'hud/elements/unitHealthBars'
		'hud/elements/unitInfo'
		'hud/elements/waveIncoming'
		'hud/elements/waveProgress'
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
			, 200
		return

	terrainBuild: (map) ->
		world = db.get 'world'
		if world.get('name') is 'Tutorial'
			tutorial = new (require 'hud/elements/tutorial') @canvas, @view
			@elements.push tutorial
			$('#build').css 'display', 'none'
			$('#stats').css 'display', 'none'
		else
			super map
		return

return GameHUD