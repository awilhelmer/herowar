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
			containerPosition = @$container.position()
			viewPosition.x = 150 if viewPosition.x < 150
			viewPosition.x = @canvas.width - 150 if viewPosition.x > @canvas.width - 150
			viewPosition.y = 150 if viewPosition.y < 150
			viewPosition.y = @canvas.height - 150 if viewPosition.y > @canvas.height - 150
			@_drawCircle viewPosition.x, viewPosition.y
			if containerPosition.top isnt viewPosition.x or containerPosition.left isnt viewPosition.y
				#console.log 'Draw new wave position', positionVec, viewPosition
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

	_drawCircle: (centerX, centerY) ->
		radius = 30
		@ctx.beginPath()
		@ctx.arc centerX, centerY, radius, 0, 2 * Math.PI, false
		@ctx.fillStyle = '#111111'
		@ctx.fill()
		@ctx.lineWidth = 4
		@ctx.strokeStyle = '#FF0000'
		@ctx.stroke()
	
return WaveIncomingHUDElement