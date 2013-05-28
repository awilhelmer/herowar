BaseView = require 'views/baseView'
templates = require 'templates'
events = require 'events'
db = require 'database'

class HudView extends BaseView

	id: 'hud'
	
	template: templates.get 'hud.tmpl'
	
	initialize: (options) ->
		@waves = db.get 'ui/waves'
		@state = 0
		super options
		
	bindEvents: ->
		events.on 'engine:render', @update, @
	
	render: ->
		super()
		@initCanvas() unless @$canvas

	initCanvas: ->
		@$canvas = @$ 'canvas'
		@canvas = @$canvas.get 0
		$body = $ 'body'
		@canvas.width = @$canvas.parent().width()
		@canvas.height = @$canvas.parent().height()
		@font = 'Arial'
		@initCtx()

	initCtx: ->
		@ctx = @canvas.getContext '2d'
		@ctx.textAlign = 'center'
		@ctx.textBaseline = 'top'
	
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

	_drawText: (content, x, y, width) ->
		baseFontSize = 24
		size = @_measureText "#{content}", @font, baseFontSize, false
		newFontSize = baseFontSize * (width / size[0])
		@ctx.font = "#{newFontSize}px #{@font}"
		@ctx.fillText "#{content}", x, y

	_drawMultilineBlockText: (content, x, y, width) ->
		splittedLines = content.split '\n'
		baseFontSize = 24
		addToY = 0
		for line in splittedLines
			size = @_measureText "#{line}", @font, baseFontSize, false
			newFontSize = baseFontSize * (width / size[0])
			@ctx.font = "#{newFontSize}px #{@font}"
			@ctx.fillText "#{line}", x, y + addToY
			size = @_measureText "#{line}", @font, newFontSize, false
			addToY += size[1]

	_setShadow: (shadowOffsetX, shadowOffsetY, shadowBlur) ->
		@ctx.shadowOffsetX = shadowOffsetX
		@ctx.shadowOffsetY = shadowOffsetY
		@ctx.shadowBlur = shadowBlur
		@ctx.shadowColor = 'rgba(0, 0, 0, 0.5)'
	
	_measureText: (text, font, size, bold) ->
		key = "#{text}:#{font}:#{size}:#{bold}"
		@__measuretext_cache__ = {} unless @__measuretext_cache__
		return @__measuretext_cache__[key] if _.has @__measuretext_cache__, key
		div = document.createElement 'div'
		div.innerHTML = text
		div.style.position = 'absolute'
		div.style.top = '-1000px'
		div.style.left = '-1000px'
		div.style.fontFamily = font
		div.style.fontWeight = if bold then 'bold' else 'normal'
		div.style.fontSize = "#{size}px"
		div.style.lineHeight = "90%"
		document.body.appendChild div
		size = [div.offsetWidth, div.offsetHeight]
		document.body.removeChild div
		@__measuretext_cache__[key] = size
		return size
		
	_gameIsInitialized: ->
		return @waves.get '_active' 
	
	_gameIsStarted: ->
		return @waves.get('current') isnt 0
	
return HudView