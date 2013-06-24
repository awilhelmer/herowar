TutorialUpdatePacket = require 'network/packets/tutorialUpdatePacket'
BaseHUDElement = require 'hud/elements/baseHudElement'
PacketType = require 'network/packets/packetType'
canvasUtils = require 'util/canvasUtils'
events = require 'events'
db = require 'database'

class TutorialHUDElement extends BaseHUDElement

	initialize: ->
		@input = db.get 'input'
		@changeState = false
		@trooper = @createTrooper()
		@alpha = 0.0
		@alphaBackground = 0.0
		@alphaContinue = 0.0
		@texts = []
		@newTexts = []
		@bindEvents()
		return
		
	bindEvents: ->
		events.listenTo @input, 'key:down', @_onTutorialContinue
		events.listenTo @input, 'mouse:down', @_onTutorialContinue
		events.on "retrieve:packet:#{PacketType.SERVER_TUTORIAL_UPDATE}", @_onTutorialUpdate, @
		return
	
	createTrooper: ->
		trooper = 
			loaded   : false
			image    : new Image()
			position : 0
		trooper.image.onload = () =>
			trooper.loaded = true
			trooper.position = @canvas.height
		trooper.image.src = 'assets/images/game/tutorial/trooper.png'
		return trooper
	
	update: (delta, now) ->
		if @trooper.loaded and (@texts.length isnt 0 || @newTexts.length isnt 0)
			@_drawBackground delta, now
			@_drawTrooper delta, now
			@_drawTutorialText delta, now, @texts
			@_drawContinueText delta, now, @texts
			@_updateAlpha delta
		return

	_updateAlpha: (delta) ->
		if @changeState
			@alpha -= delta * 2
			@alphaBackground -= @alpha if @newTextslength is 0
			if @alpha <= 0
				@alpha = 0
				@alphaContinue = 0.0
				@alphaBackground -= 0.0 if @newTextslength is 0
				@alphaContinueType = 0.0
				@changeState = false
				@texts = @newTexts
		else
			if @alpha < 1.0
				@alpha += delta * 2
				@alpha = 1.0 if @alpha > 1.0
			@alphaBackground = @alpha if @alphaBackground < 1.0
		return
	
	_drawBackground: (delta, now) ->
		@ctx.fillStyle = "rgba(0, 104, 175, #{@alphaBackground / 2})"
		@ctx.strokeStyle = "rgba(141, 167, 204, #{@alphaBackground})"
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
		position = 
			x: @canvas.width - @trooper.image.width + 250
			y: @canvas.height - @trooper.image.height * 0.5
		if @trooper.position > position.y
			@trooper.position -= delta * @canvas.height
			position.y = @trooper.position if @trooper.position > position.y
		@ctx.drawImage @trooper.image, position.x, position.y
		return

	_drawTutorialText: (delta, now, texts) ->
		size =
			x: @canvas.width - @trooper.image.width + 200 - 400
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
		@ctx.fillText 'Press any key to continue...', @canvas.width - @trooper.image.width, @canvas.height - 50
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

	_onTutorialContinue: (event) =>
		if @trooper.loaded and @alpha is 1.0 
			events.trigger 'send:packet', new TutorialUpdatePacket() 
		return

	_onTutorialUpdate: (packet) ->
		console.log '_onTutorialUpdate', packet
		if packet?.texts?.length isnt 0
			@newTexts = packet.texts 
		else
			@newTexts = []
		@changeState = true
		@requestUpdate = false
		return
	
return TutorialHUDElement