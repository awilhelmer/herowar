ObjectHUDElement = require 'hud/elements/objectInfo'
objectUtils = require 'util/objectUtils'
events = require 'events'

class UnitInfoHUDElement extends ObjectHUDElement
	
	eventType: 'unit'
	
	size: 
		w: 160
		h : 80
	
	_drawObjectInfo: (delta, now) ->
		super delta, now
		if (@object.currentHealth is 0 or @object.isDisposed) and @alpha > 0
			@alpha -= delta * 1.5
			@alpha = 0 if @alpha < 0
		return
	
	_drawInfoText: (offset) ->
		@ctx.fillStyle = "rgba(255, 255, 255, #{@alpha})"
		@ctx.font = 'bold 19px Arial'
		@ctx.fillText @object.name, offset.x + @size.w / 2, offset.y + 10
		@ctx.font = '14px Arial'
		@ctx.fillText "Health: #{@object.currentHealth} / #{@object.maxHealth}", offset.x + @size.w / 2, offset.y + 35
		@ctx.fillText "Shield: #{@object.currentShield} / #{@object.maxShield}", offset.x + @size.w / 2, offset.y + 55
		return

	_select: (@object) ->
		super @object
		@alpha = 1.0
		return

return UnitInfoHUDElement