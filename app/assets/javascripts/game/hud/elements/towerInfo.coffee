BaseHUDElement = require 'hud/elements/baseHudElement'
objectUtils = require 'util/objectUtils'
events = require 'events'

class TowerInfoHUDElement extends BaseHUDElement
	
	initialize: ->
		@bindEvents()
		@size = w : 160, h : 100
		return
	
	bindEvents: ->
		events.on 'tower:select', @_select, @
		events.on 'tower:deselect', @_deselect, @
		return
	
	update: (delta, now) ->
		return unless @tower
		@_drawTowerInfo()
		return
	
	_drawTowerInfo: ->
		viewportWidthHalf = @canvas.width / 2
		viewportHeightHalf = @canvas.height / 2
		position = objectUtils.positionToScreen @tower, viewportWidthHalf, viewportHeightHalf, @view.get 'cameraScene'
		offset = @_calculateOffset position
		@_drawBackground offset
		@_drawInfoText offset
		return

	_drawBackground: (offset) ->
		@ctx.beginPath()
		@ctx.fillStyle = 'rgba(0, 0, 0, 0.5)'
		@ctx.rect offset.x, offset.y, @size.w, @size.h
		@ctx.closePath()
		@ctx.fill()
		return
		
	_drawInfoText: (offset) ->
		@ctx.fillStyle = '#ffffff'
		@ctx.font = 'bold 19px Arial'
		@ctx.fillText @tower.name, offset.x + @size.w / 2, offset.y + 10
		@ctx.font = '14px Arial'
		@ctx.fillText "Owner: #{@tower.attributes.owner.name}", offset.x + @size.w / 2, offset.y + 35
		@ctx.fillText "Kills: #{@tower.attributes.kills}", offset.x + @size.w / 2, offset.y + 55
		@ctx.fillText "Gold:  #{@tower.attributes.rewardGold}", offset.x + @size.w / 2, offset.y + 75
		return
	
	_calculateOffset: (position) ->
		return x: position.x - @size.w / 2, y: position.y - @size.h
	
	_select: (@tower) ->
		return
		
	_deselect: ->
		delete @tower
		return
	
return TowerInfoHUDElement