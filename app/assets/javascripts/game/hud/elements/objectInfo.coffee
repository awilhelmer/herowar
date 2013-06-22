BaseHUDElement = require 'hud/elements/baseHudElement'
objectUtils = require 'util/objectUtils'
events = require 'events'

class ObjectInfoHUDElement extends BaseHUDElement

	initialize: ->
		@bindEvents()
		return
	
	bindEvents: ->
		events.on "#{@eventType}:select", @_select, @
		events.on "#{@eventType}:deselect", @_deselect, @
		return

	update: (delta, now) ->
		return unless @object
		@_drawObjectInfo delta, now
		return
	
	_drawObjectInfo: (delta, now) ->
		viewportWidthHalf = @canvas.width / 2
		viewportHeightHalf = @canvas.height / 2
		position = objectUtils.positionCenterToScreen @object, viewportWidthHalf, viewportHeightHalf, @view.get 'cameraScene'
		offset = @_calculateOffset position
		@_drawBackground offset
		@_drawInfoText offset
		return

	_drawBackground: (offset) ->
		@ctx.beginPath()
		@ctx.fillStyle = "rgba(0, 0, 0, #{@alpha / 2})"
		@ctx.rect offset.x, offset.y, @size.w, @size.h
		@ctx.closePath()
		@ctx.fill()
		return

	_calculateOffset: (position) ->
		return x: position.x - @size.w / 2, y: position.y - @size.h - @size.h
	
	_select: (@object) ->
		return
		
	_deselect: ->
		delete @object
		return
	
return ObjectInfoHUDElement