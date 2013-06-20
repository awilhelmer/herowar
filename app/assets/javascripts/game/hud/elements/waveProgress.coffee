BaseHUDElement = require 'hud/elements/baseHudElement'
canvasUtils = require 'util/canvasUtils'
db = require 'database'

class WaveProgressHUDElement extends BaseHUDElement
			
	initialize: ->
		@waves = db.get 'ui/waves'
		@alpha = 1.0
		@startInfo = true
		return
	
	update: (delta, now) ->
		return unless @waves.get '_active'
		@_drawGameStartInfo delta if @startInfo
		@_drawWaveProgress()
		return
	
	_drawGameStartInfo: (delta) ->
		@ctx.textAlign = 'center'
		if @waves.get('current') is 0 and @waves.get('total') isnt 0 and @waves.get('positions').length is 0
			# Preparing game
			width = @canvas.height / 5
			canvasUtils.setShadow @ctx, width / 50, width / 50, width / 15
			@ctx.fillStyle = 'rgba(255, 255, 255, 1.0)'
			canvasUtils.drawMultilineBlockText @ctx, "GAME\nSTART\n#{@waves.get('arrival_short')}\nSECONDS", @getHalfWidth(), @getHalfHeight() / 2, width
		else if @waves.get('current') isnt 0 
			# Game starting
			width = @canvas.height / 5
			canvasUtils.setShadow @ctx, width / 50, width / 50, width / 15
			@ctx.fillStyle = "rgba(255, 255, 255, #{@alpha})"
			canvasUtils.drawText @ctx, "GAME START", @getHalfWidth(), @getHalfHeight() / 2, ((1 - @alpha) * width * 2) + width * 2
			@alpha = @alpha - delta if delta
			@startInfo = false if @alpha <= 0
	
	_drawWaveProgress: ->
		current = @waves.get 'current'
		total = @waves.get 'total'
		arrival = @waves.get 'arrival'
		width = @canvas.height / 10
		if arrival and @waves.get('positions').length is 0
			text = "#{current} / #{total} - #{arrival}"
		else
			text = "#{current} / #{total}"
		canvasUtils.setShadow @ctx, width / 50, width / 50, width / 15
		@ctx.fillStyle = 'rgba(255, 255, 255, 1.0)'
		canvasUtils.drawText @ctx, text, @getHalfWidth(), 10, width
		return
	
return WaveProgressHUDElement