ObjectHUDElement = require 'hud/elements/objectInfo'
objectUtils = require 'util/objectUtils'
events = require 'events'

class TowerInfoHUDElement extends ObjectHUDElement
	
	eventType: 'tower'
	
	size: 
		w: 160
		h : 100

	_drawInfoText: (offset) ->
		@ctx.fillStyle = "rgba(255, 255, 255, #{@alpha})"
		@ctx.font = 'bold 19px Arial'
		@ctx.fillText @object.name, offset.x + @size.w / 2, offset.y + 10
		@ctx.font = '14px Arial'
		@ctx.fillText "Owner: #{@object.attributes.owner.name}", offset.x + @size.w / 2, offset.y + 35
		@ctx.fillText "Kills: #{@object.attributes.kills}", offset.x + @size.w / 2, offset.y + 55
		@ctx.fillText "Gold:  #{@object.attributes.rewardGold}", offset.x + @size.w / 2, offset.y + 75
		return
	
return TowerInfoHUDElement