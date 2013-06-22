BaseHUDElement = require 'hud/elements/baseHudElement'
PacketType = require 'network/packets/packetType'
canvasUtils = require 'util/canvasUtils'
events = require 'events'
db = require 'database'

class TutorialHUDElement extends BaseHUDElement

	initialize: ->
		@input = db.get 'input'
		@state = -1
		@changeState = false
		@trooperImageLoaded = false
		@trooperImage = new Image()
		@trooperImage.onload = () =>
			@trooperImageLoaded = true
		@trooperImage.src = 'assets/images/game/tutorial/trooper.png'
		@alpha = 0.0
		@alphaContinue = 0.0
		@texts = []
		@bindEvents()
		return
		
	bindEvents: ->
		events.listenTo @input, 'mouse:down', @_onMouseDown
		events.on "retrieve:packet:#{PacketType.SERVER_TUTORIAL_UPDATE}", @_onTutorialUpdate, @
		return
	
	update: (delta, now) ->
		@_drawInfo delta, now, @texts if @trooperImageLoaded and @texts.length isnt 0
		return
	
	_drawInfo: (delta, now, texts) ->
		if @state isnt 8
			@_drawBackground delta, now
			@_drawTrooper delta, now
			@_drawTutorialText delta, now, texts
			@_drawContinueText delta, now, texts
		@_updateAlpha delta
		return

	_updateAlpha: (delta) ->
		if @changeState
			if @alpha > 0
				@alpha -= delta
				if @alpha <= 0
					@alpha = 0
					@alphaContinue = 0.0
					@alphaContinueType = 0
					@state++
					@changeState = false
		else
			if @alpha < 1.0
				@alpha += delta 
				@alpha = 1.0 if @alpha > 1.0
		return
	
	_drawBackground: (delta, now) ->
		@ctx.fillStyle = "rgba(0, 104, 175, #{@alpha / 2})"
		@ctx.strokeStyle = "rgba(141, 167, 204, #{@alpha})"
		@ctx.lineWidth = 4
		@ctx.beginPath()
		@ctx.moveTo @canvas.width + 10, @canvas.height + 10
		@ctx.lineTo @canvas.width - 500, @canvas.height + 10
		@ctx.lineTo @canvas.width + 10, @canvas.height - 150
		@ctx.lineTo @canvas.width + 10, @canvas.height + 10
		@ctx.fill()
		@ctx.stroke()
		@ctx.closePath()
		return
	
	_drawTrooper: (delta, now) ->
		@ctx.drawImage @trooperImage, @canvas.width - @trooperImage.width + 250, @canvas.height - @trooperImage.height * 0.5
		return

	_drawTutorialText: (delta, now, texts) ->
		size =
			x: @canvas.width - @trooperImage.width + 200 - 400
			y: @canvas.height - texts.length * 20 - 100
			w: 400
			h: 20 + texts.length * 20
			position: 'top-right'
		@ctx.beginPath()
		@ctx.fillStyle = "rgba(255, 255, 255, #{@alpha})"
		@ctx.strokeStyle = "rgba(0, 0, 0, #{@alpha})"
		@ctx.lineWidth = 1
		canvasUtils.drawBubble @ctx, size
		@ctx.closePath()
		@ctx.fill()
		@ctx.stroke()
		@ctx.fillStyle = "rgba(0, 0, 0, #{@alpha})"
		@ctx.font = 'bold 16px Arial'
		for text in texts
			@ctx.fillText text, size.x + size.w / 2, size.y + 10
			size.y += 20
		return

	_drawContinueText: (delta, now, texts) ->
		@_updateContinueAlpha delta if @alpha is 1
		alpha = if @alpha < @alphaContinue then @alpha else @alphaContinue
		@ctx.fillStyle = "rgba(0, 0, 0, #{alpha})"
		@ctx.font = 'bold 16px Arial'
		@ctx.fillText 'Press any key to continue...', @canvas.width - @trooperImage.width, @canvas.height - 50
		return

	_updateContinueAlpha: (delta) ->
		if @alphaContinueType is 1
			@alphaContinue -= delta
			if @alphaContinue <= 0
				@alphaContinue = 0
				@alphaContinueType = 2
		else
			@alphaContinue += delta
			if @alphaContinue >= 1
				@alphaContinue = 1
				@alphaContinueType = 1
		return

	_onMouseDown: (event) =>
		@changeState = true if @trooperImageLoaded and @alpha is 1.0 and not @changeState
		return

	_onTutorialUpdate: (packet) ->
		console.log '_onTutorialUpdate', packet
		@state = packet.state if packet?.state
		@texts = packet.texts if packet?.texts?.length isnt 0
		return
	
return TutorialHUDElement