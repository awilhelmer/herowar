BaseHUDElement = require 'hud/elements/baseHudElement'
canvasUtils = require 'util/canvasUtils'
viewUtils = require 'util/viewUtils'
db = require 'database'

class WaveIncomingHUDElement extends BaseHUDElement
	
	constructor: (canvas, view) ->
		super canvas, view
		@initialize()
		
	initialize: ->
		@waves = db.get 'ui/waves'
		@$container = $ '<div class="wave-position"></div>'
		$('body').append @$container
		@skullImageLoaded = false
		@skullImage = new Image()
		@skullImage.onload = () =>
			@skullImageLoaded = true
		@skullImage.src = 'assets/images/game/ui/skull.png'
		@scale = 1
		@scaleType = 1
		return
	
	update: (delta, now) ->
		return unless @waves.get '_active'
		@_showWaveIncoming delta
		return
	
	_showWaveIncoming: (delta) ->
		positions = @waves.get 'positions'
		if positions?.length isnt 0
			position = @_convertWaypointToCanvasCoords positions[0] # TODO: show other positions also since it could be more than one start point for the next wave...
			limitedTo = @_limitToScreen position
			@_drawDirection position, limitedTo if limitedTo isnt ''
			@_drawIcon position
			@_drawStartInfo position, limitedTo
			@_updateScale delta
			containerPosition = @$container.position()
			if containerPosition.left isnt position.x or containerPosition.top isnt position.y
				#console.log 'Draw new wave position', limitedTo, positionVec, viewPosition
				@$container.css
					'left' : "#{position.x}px"
					'top' : "#{position.y}px"
			@$container.removeClass 'hidden' if @$container.hasClass 'hidden'
		else
			if not @$container.hasClass 'hidden'
				@$container.addClass 'hidden' 
				@$container.css
					'top' : ''
					'left' : ''
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
		radius = 30
		@ctx.beginPath()
		@ctx.fillStyle = '#111111'
		@ctx.strokeStyle = '#FF0000'
		@ctx.lineWidth = 4
		@ctx.arc position.x, position.y, radius, 0, 2 * Math.PI
		@ctx.closePath()
		@ctx.fill()
		@ctx.stroke()
		@ctx.drawImage @skullImage, position.x - radius * 0.75, position.y - radius * 0.75, radius * 1.5, radius * 1.5 if @skullImageLoaded
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
		@ctx.textAlign = 'left'
		@ctx.font = 'bold 19px Arial'
		@ctx.fillText 'START BATTLE!', size.x + 10, size.y + 10
		@ctx.font = 'bold 14px Arial'
		@ctx.fillText 'CLICK TO CALL WAVE', size.x + 10, size.y + 35
		return
	
return WaveIncomingHUDElement