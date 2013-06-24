BaseHUDElement = require 'hud/elements/baseHudElement'
PacketType = require 'network/packets/packetType'
viewUtils = require 'util/viewUtils'
events = require 'events'

class TowerRestrictionHUDElement extends BaseHUDElement

	size: 
		w : 200
		h : 40

	initialize: ->
		@towerRestrictions = []
		@alpha = 1.0
		@bindEvents()
		return
	
	bindEvents: ->
		events.on "retrieve:packet:#{PacketType.SERVER_TOWER_RESTRICTION}", @_addTowerRestriction, @
		return

	update: (delta, now) ->
		return if @towerRestrictions.length is 0
		@_drawTowerRestriction @towerRestrictions[0], delta, now
		return
	
	_drawTowerRestriction: (position, delta, now) ->
		positionVec = new THREE.Vector3 position.x, position.y, position.z
		viewportWidthHalf = @canvas.width / 2
		viewportHeightHalf = @canvas.height / 2
		position = viewUtils.positionToScreen positionVec, viewportWidthHalf, viewportHeightHalf, @view.get 'cameraScene'
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

	_drawInfoText: (offset) ->
		@ctx.fillStyle = "rgba(255, 255, 255, #{@alpha})"
		@ctx.font = 'bold 19px Arial'
		@ctx.fillText "Place tower here!", offset.x + @size.w / 2, offset.y + 10
		return

	_calculateOffset: (position) ->
		return x: position.x - @size.w / 2, y: position.y - @size.h - @size.h

	_addTowerRestriction: (packet) ->
		console.log '_onTowerRestriction', packet
		unless @towerRestrictions.length is 0
			res = @towerRestrictions[0]
			@towerRestrictions.length = 0
		return unless packet.position
		@towerRestrictions.push packet.position
		return
	
return TowerRestrictionHUDElement