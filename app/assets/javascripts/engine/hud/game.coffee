BaseHUD = require 'hud/baseHud'
db = require 'database'

class GameHUD extends BaseHUD
	
	initialize: (options) ->
		@waves = db.get 'ui/waves'
		@state = 0

	update: (delta) ->
		return unless @waves.get('_active') and @waves.get('arrival_short')
		screen_hw = @canvas.width / 2
		screen_hh = @canvas.height / 2
		@ctx.clearRect 0, 0, @canvas.width, @canvas.height
		switch @state
			when 0
				# State: Preparing game
				width = @canvas.height / 5
				@_setShadow width / 50, width / 50, width / 15
				@ctx.fillStyle = 'rgba(255, 255, 255, 1.0)'
				if @_gameIsStarted()
					@state++
					@alpha = 1.0
				else
					@_drawMultilineBlockText "GAME\nSTART\n#{@waves.get('arrival_short')}\nSECONDS", screen_hw, screen_hh / 2, width
			when 1
				# State: Game starting
				width = @canvas.height / 5
				@_setShadow width / 50, width / 50, width / 15
				@ctx.fillStyle = "rgba(255, 255, 255, #{@alpha})"
				@_drawMultilineBlockText "GAME START", screen_hw, screen_hh / 2, ((1 - @alpha) * width * 4) + width * 2
				@alpha = @alpha - delta
				@state++ if @alpha <= 0

	_gameIsInitialized: ->
		return @waves.get '_active' 
	
	_gameIsStarted: ->
		return @waves.get('current') isnt 0

return GameHUD