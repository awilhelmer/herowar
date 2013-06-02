BaseHUDElement = require 'hud/elements/baseHudElement'
canvasUtils = require 'util/canvasUtils'
db = require 'database'

class WaveProgressHUDElement extends BaseHUDElement
	
	constructor: (canvas, view) ->
		super canvas, view
		@waves = db.get 'ui/waves'
		@alpha = 1.0
		@startInfo = true
	
	update: (delta, now) ->
		return unless @waves.get '_active'
		@_drawGameStartInfo delta if @startInfo
		@_drawWaveProgress()
		return
	
	_drawGameStartInfo: (delta) ->
		if @waves.get('current') is 0 and @waves.get('total') isnt 0
			# Preparing game
			width = @canvas.height / 5
			canvasUtils.setShadow @ctx, width / 50, width / 50, width / 15
			@ctx.fillStyle = 'rgba(255, 255, 255, 1.0)'
			canvasUtils.drawMultilineBlockText @ctx, "GAME\nSTART\n#{@waves.get('arrival_short')}\nSECONDS", @getHalfWidth(), @getHalfHeight() / 2, width
		else
			# Game starting
			width = @canvas.height / 5
			canvasUtils.setShadow @ctx, width / 50, width / 50, width / 15
			@ctx.fillStyle = "rgba(255, 255, 255, #{@alpha})"
			canvasUtils.drawText @ctx, "GAME START", @getHalfWidth(), @getHalfHeight() / 2, ((1 - @alpha) * width * 4) + width * 2
			@alpha = @alpha - delta if delta
			@startInfo = false if @alpha <= 0
	
	_drawWaveProgress: ->
		current = @waves.get 'current'
		total = @waves.get 'total'
		arrival = @waves.get 'arrival'
		width = @canvas.height / 10
		if arrival
			text = "#{current} / #{total} - #{arrival}"
		else
			text = "#{current} / #{total}"
		canvasUtils.setShadow @ctx, width / 50, width / 50, width / 15
		@ctx.fillStyle = 'rgba(255, 255, 255, 1.0)'
		canvasUtils.drawText @ctx, text, @getHalfWidth(), 10, width
		return
	
return WaveProgressHUDElement