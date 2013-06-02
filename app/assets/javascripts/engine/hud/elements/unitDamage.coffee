BaseHUDElement = require 'hud/elements/baseHudElement'
objectUtils = require 'util/objectUtils'
canvasUtils = require 'util/canvasUtils'

class UnitDamageHudElement extends BaseHUDElement
	
	maxAge: 1500
	
	constructor: (canvas, view, @unit, @damage) ->
		super canvas, view
		@positionWorld = @unit.getMainObject().position
		@positionScreen = objectUtils.positionToScreen @unit, @getHalfWidth(), @getHalfHeight(), view.get 'cameraScene'
		@birthDate	= Date.now()

	update: (delta, now) ->
		age	= now - @birthDate
		opacity = 1 - age / @maxAge
		@active = false if age >= @maxAge
		width = @canvas.height / 20
		canvasUtils.setShadow @ctx, 0, 0, 0
		@ctx.fillStyle = "rgba(205,24,31, #{opacity})"
		canvasUtils.drawText @ctx, "-#{@damage}", @positionScreen.x, @positionScreen.y, width
		@positionScreen.y -= 2
		return

return UnitDamageHudElement