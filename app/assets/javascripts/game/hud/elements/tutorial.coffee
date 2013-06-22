BaseHUDElement = require 'hud/elements/baseHudElement'
canvasUtils = require 'util/canvasUtils'

class TutorialHUDElement extends BaseHUDElement

	initialize: ->
		@state = 1
		@trooperImageLoaded = false
		@trooperImage = new Image()
		@trooperImage.onload = () =>
			@trooperImageLoaded = true
		@trooperImage.src = 'assets/images/game/tutorial/trooper.png'
		#@alphaContinueType = 1
		@alphaContinue = 0.0
		return
	
	update: (delta, now) ->
		if @trooperImageLoaded 
			@_drawInfo delta, now, ['Hello Rekrut and Welcome to Herowar. I will help', 'you a bit to understand how to play this game.']
		return
	
	_drawInfo: (delta, now, texts) ->
		@_drawBackground delta, now
		@_drawTrooper delta, now
		@_drawTutorialText delta, now, texts
		@_drawContinueText delta, now, texts
		return
	
	_drawBackground: (delta, now) ->
		@ctx.fillStyle = 'rgba(0, 104, 175, 0.5)'
		@ctx.strokeStyle = 'rgb(141, 167, 204)'
		@ctx.lineWidth = 4
		@ctx.beginPath()
		@ctx.moveTo @canvas.width + 10, @canvas.height + 10
		@ctx.lineTo @canvas.width - 600, @canvas.height + 10
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
		@ctx.fillStyle = "rgba(0, 0, 0, #{@alphaContinue})"
		@ctx.font = 'bold 16px Arial'
		@ctx.fillText 'Press any key to continue...', @canvas.width - @trooperImage.width, @canvas.height - 50
		return
	
return TutorialHUDElement