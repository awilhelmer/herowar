BaseHUDElement = require 'hud/elements/baseHudElement'
canvasUtils = require 'util/canvasUtils'
viewUtils = require 'util/viewUtils'
events = require 'events'
db = require 'database'

class WaveIncomingHUDElement extends BaseHUDElement
	
	constructor: (canvas, view) ->
		super canvas, view
		@initialize()
		
	initialize: ->
		@waves = db.get 'ui/waves'
		@input = db.get 'input'
		@skullImageLoaded = false
		@skullImage = new Image()
		@skullImage.onload = () =>
			@skullImageLoaded = true
		@skullImage.src = 'assets/images/game/ui/skull.png'
		@scale = 1
		@scaleType = 1
		@iconRadius = 30
		@isHovering = false
		@nextWaveEnemies = []
		return
	
	update: (delta, now) ->
		return unless @waves.get '_active'
		@_showWaveIncoming delta, now
		return
	
	_showWaveIncoming: (delta, now) ->
		positions = @waves.get 'positions'
		if positions?.length isnt 0
			position = @_convertWaypointToCanvasCoords positions[0] # TODO: show other positions also since it could be more than one start point for the next wave...
			limitedTo = @_limitToScreen position
			@_drawDirection position, limitedTo if limitedTo isnt ''
			@_drawIcon position
			@_drawStartInfo position, limitedTo if @waves.get('current') is 0
			@_updateHovering now, position, limitedTo
			@_updateScale delta
		return

	_convertWaypointToCanvasCoords: (position) ->
		positionVec = new THREE.Vector3 position.x, position.y, position.z
		viewportWidthHalf = @canvas.width / 2
		viewportHeightHalf = @canvas.height / 2
		return viewUtils.positionToScreen positionVec, viewportWidthHalf, viewportHeightHalf, @view.get 'cameraScene'

	_limitToScreen: (position) ->
		limitedTo = ''
		if position.y < 150 
			position.y = 150
			limitedTo = 'top'
		else if position.y > @canvas.height - 150
			position.y = @canvas.height - 150
			limitedTo = 'bottom'
		if position.x < 150
			position.x = 150
			limitedTo = if limitedTo is '' then 'left' else "#{limitedTo}-left"
		else if position.x > @canvas.width - 150
			position.x = @canvas.width - 150 
			limitedTo = if limitedTo is '' then 'right' else "#{limitedTo}-right"
		return limitedTo

	_updateScale: (delta) ->
		if @scaleType is 1
			@scale += delta / 7.5
			if @scale > 1.05
				@scale = 1.05
				@scaleType = 2
		else
			@scale -= delta / 2
			if @scale < 1
				@scale = 1
				@scaleType = 1		
		return		

	_drawDirection: (position, limitedTo) ->
		@ctx.beginPath()
		@ctx.fillStyle = '#111111'
		switch limitedTo
			when 'top'
				@ctx.moveTo position.x - 20, position.y
				@ctx.lineTo position.x, position.y - 60
				@ctx.lineTo position.x + 20, position.y
			when 'bottom'
				@ctx.moveTo position.x - 20, position.y
				@ctx.lineTo position.x, position.y + 60
				@ctx.lineTo position.x + 20, position.y
			when 'left'
				@ctx.moveTo position.x, position.y - 20
				@ctx.lineTo position.x - 60, position.y
				@ctx.lineTo position.x, position.y + 20
			when 'right'
				@ctx.moveTo position.x, position.y - 20
				@ctx.lineTo position.x + 60, position.y
				@ctx.lineTo position.x, position.y + 20
			when 'top-left'
				@ctx.moveTo position.x - 20, position.y
				@ctx.lineTo position.x - 40, position.y - 40
				@ctx.lineTo position.x, position.y - 20
			when 'bottom-left'
				@ctx.moveTo position.x - 20, position.y
				@ctx.lineTo position.x - 40, position.y + 40
				@ctx.lineTo position.x, position.y + 20
			when 'top-right'
				@ctx.moveTo position.x + 20, position.y
				@ctx.lineTo position.x + 40, position.y - 40
				@ctx.lineTo position.x, position.y - 20
			when 'bottom-right'
				@ctx.moveTo position.x + 20, position.y
				@ctx.lineTo position.x + 40, position.y + 40
				@ctx.lineTo position.x, position.y + 20
		@ctx.closePath()
		@ctx.fill()
		return

	_drawIcon: (position) ->
		# Incoming eta
		@ctx.beginPath()
		@ctx.fillStyle = '#FF0000'
		radius = 2 * Math.PI
		if @waves.get('eta')
			perc = (Date.now() - @waves.get('start')) / (@waves.get('eta') - @waves.get('start'))
			if perc <= 1
				console.log 'Now', Date.now() - @waves.get('start'), 'Eta', @waves.get('eta') - @waves.get('start'), 'Value', (Date.now() - @waves.get('start')) / (@waves.get('eta') - @waves.get('start'))
				radius *= perc
		@ctx.arc position.x, position.y, @iconRadius, 0, radius
		@ctx.closePath()
		@ctx.fill()
		# Incoming eta border
		@ctx.beginPath()
		@ctx.strokeStyle = '#000000'
		@ctx.lineWidth = 1
		@ctx.arc position.x, position.y, @iconRadius, 0, 2 * Math.PI
		@ctx.closePath()
		@ctx.stroke()
		# Icon background
		@ctx.beginPath()
		@ctx.fillStyle = '#111111'
		@ctx.arc position.x, position.y, @iconRadius - 4, 0, 2 * Math.PI
		@ctx.closePath()
		@ctx.fill()
		
		@ctx.drawImage @skullImage, position.x - @iconRadius * 0.75, position.y - @iconRadius * 0.75, @iconRadius * 1.5, @iconRadius * 1.5 if @skullImageLoaded
		return

	_drawStartInfo: (position, limitedTo) ->
		align = if limitedTo.indexOf('left') != -1 then 'left' else 'right'
		size =
			x: if align is 'left' then position.x + 225 else position.x - 225
			y: position.y - 30
			w: 175
			h: 60
		@ctx.beginPath()
		@ctx.fillStyle = '#ffffff'
		@ctx.strokeStyle = '#000000'
		@ctx.lineWidth = 1
		canvasUtils.drawBubble @ctx,
			x: size.x - (size.w * @scale - size.w) / 2
			y: size.y - (size.h * @scale - size.h) / 2
			w: size.w * @scale
			h: size.h * @scale
			position: align
		@ctx.closePath()
		@ctx.fill()
		@ctx.stroke()
		@ctx.fillStyle = '#000000'
		@ctx.font = 'bold 19px Arial'
		@ctx.fillText 'START BATTLE!', size.x + size.w / 2, size.y + 10
		@ctx.font = 'bold 14px Arial'
		@ctx.fillText 'CLICK TO CALL WAVE', size.x + size.w / 2, size.y + 35
		return

	_drawWaveInfo: (position, limitedTo, units) ->
		height = 65 + units.length * 20
		size = 
			x: if limitedTo.indexOf('left') != -1 then position.x + 225 else position.x - 225
			y: if limitedTo.indexOf('top') != -1 then position.y + @iconRadius + 5 else position.y - (height + @iconRadius + 5)
			w: 175
			h: height
		@ctx.beginPath()
		@ctx.fillStyle = 'rgba(0, 0, 0, 0.5)'
		@ctx.rect size.x, size.y, size.w, size.h
		@ctx.closePath()
		@ctx.fill()
		@ctx.fillStyle = '#ffffff'
		@ctx.font = 'bold 14px Arial'
		newY = size.y + 10
		@ctx.fillText 'INCOMING WAVE', size.x + size.w / 2, newY
		newY += 5
		for wave in units
			newY += 20
			@ctx.fillText wave, size.x + size.w / 2, newY 
		@ctx.font = 'bold 12px Arial'
		@ctx.fillText 'CLICK TO CALL IT EARLY', size.x + size.w / 2, newY + 25
		return

	_updateHovering: (now, position, limitedTo) ->
		if @_isHovering position
			units = @waves.get 'units'
			@_drawWaveInfo position, limitedTo, units if units?.length isnt 0
			events.trigger 'call:wave' if @input.get('mouse_pressed_left') and now - @waves.get('start') > 2000
			unless @isHovering
				document.body.style.cursor = 'pointer' 
				@isHovering = true
		else
			if @isHovering
				document.body.style.cursor = 'default' 
				@isHovering = false		
		return

	_isHovering: (position) ->
		return @input.get('mouse_position_x') >= position.x - @iconRadius and @input.get('mouse_position_x') <= position.x + @iconRadius and @input.get('mouse_position_y') >= position.y - @iconRadius and @input.get('mouse_position_y') <= position.y + @iconRadius

return WaveIncomingHUDElement