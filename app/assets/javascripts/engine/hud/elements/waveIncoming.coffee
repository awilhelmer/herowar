BaseHUDElement = require 'hud/elements/baseHudElement'
viewUtils = require 'util/viewUtils'
db = require 'database'

class WaveIncomingHUDElement extends BaseHUDElement
	
	constructor: (canvas, view) ->
		super canvas, view
		@waves = db.get 'ui/waves'
		@$container = $ '<div class="wave-position"></div>'
		$('body').append @$container
	
	update: (delta, now) ->
		return unless @waves.get '_active'
		@_showWaveIncoming delta
		return
	
	_showWaveIncoming: (delta) ->
		positions = @waves.get 'positions'
		if positions?.length isnt 0
			position = positions[0] # TODO: show other positions also since it could be more than one start point for the next wave...
			positionVec = new THREE.Vector3 position.x, position.y, position.z
			viewportWidthHalf = @canvas.width / 2
			viewportHeightHalf = @canvas.height / 2
			viewPosition = viewUtils.positionToScreen positionVec, viewportWidthHalf, viewportHeightHalf, @view.get 'cameraScene'
			limitedTo = @_limitToScreen viewPosition
			@_drawDirection viewPosition, limitedTo if limitedTo isnt ''
			@_drawIcon viewPosition, limitedTo
			containerPosition = @$container.position()
			if containerPosition.left isnt viewPosition.x or containerPosition.top isnt viewPosition.y
				#console.log 'Draw new wave position', limitedTo, positionVec, viewPosition
				@$container.css
					'left' : "#{viewPosition.x}px"
					'top' : "#{viewPosition.y}px"
			@$container.removeClass 'hidden' if @$container.hasClass 'hidden'
		else
			if not @$container.hasClass 'hidden'
				@$container.addClass 'hidden' 
				@$container.css
					'top' : ''
					'left' : ''
		return

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

	_drawIcon: (position) ->
		radius = 30
		@ctx.beginPath()
		@ctx.arc position.x, position.y, radius, 0, 2 * Math.PI, false
		@ctx.fillStyle = '#111111'
		@ctx.fill()
		@ctx.lineWidth = 4
		@ctx.strokeStyle = '#FF0000'
		@ctx.stroke()
		return

	_drawDirection: (position, limitedTo) ->
		@ctx.beginPath()
		@ctx.fillStyle = '#111111'
		switch limitedTo
			when 'top'
				@ctx.moveTo position.x - 20, position.y
				@ctx.lineTo position.x, position.y + 60
				@ctx.lineTo position.x + 20, position.y
			when 'bottom'
				@ctx.moveTo position.x - 20, position.y
				@ctx.lineTo position.x, position.y - 60
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
		@ctx.fill()
		return
	
return WaveIncomingHUDElement