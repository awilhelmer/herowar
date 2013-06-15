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
			@_drawStartInfo viewPosition, limitedTo
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
		@ctx.fill()
		return

	_drawIcon: (position) ->
		radius = 30
		@ctx.beginPath()
		@ctx.fillStyle = '#111111'
		@ctx.strokeStyle = '#FF0000'
		@ctx.lineWidth = 4
		@ctx.arc position.x, position.y, radius, 0, 2 * Math.PI, false
		@ctx.fill()
		@ctx.stroke()
		return

	_drawStartInfo: (position, limitedTo) ->
		left = if limitedTo is 'left' or limitedTo is 'top-left' or limitedTo is 'bottom-left' then position.x + 245 else position.x - 245
		top = position.y - 35
		@ctx.beginPath()
		@ctx.fillStyle = '#ffffff'
		@ctx.rect left, top, 200, 70
		@ctx.fill()
		@ctx.lineWidth = 1
		@ctx.strokeStyle = '#000000'
		@ctx.stroke()
		@ctx.fillStyle = '#000000'
		@ctx.textAlign = 'left'
		@ctx.font = 'bold 20px Arial'
		@ctx.fillText 'START BATTLE!', left + 10, top + 10
		@ctx.font = 'bold 16px Arial'
		@ctx.fillText 'CLICK TO CALL WAVE', left + 10, top + 40
		return
	
return WaveIncomingHUDElement