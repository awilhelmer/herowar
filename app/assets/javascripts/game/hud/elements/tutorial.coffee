BaseHUDElement = require 'hud/elements/baseHudElement'

class TutorialHUDElement extends BaseHUDElement

	initialize: ->
		@state = 1
		@trooperImageLoaded = false
		@trooperImage = new Image()
		@trooperImage.onload = () =>
			@trooperImageLoaded = true
		@trooperImage.src = 'assets/images/game/tutorial/trooper.png'
		return
	
	update: (delta, now) ->
		@_drawTrooper delta, now
		return
	
	_drawTrooper: (delta, now) ->
		@ctx.drawImage @trooperImage, @canvas.width - @trooperImage.width + 250, @canvas.height - @trooperImage.height * 0.75 if @trooperImage 
		return
	
return TutorialHUDElement