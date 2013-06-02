events = require 'events'

class BaseHUD
	
	default: []
	
	constructor: (@view) ->
		@elements = []
		@bindEvents()
		@createCanvas()
		@initialize()
		@elements.push new (require val) @canvas, @view  for val in @default
	
	bindEvents: ->
		events.on 'engine:render', @update, @

	initialize: ->

	createCanvas: ->
		@canvas = document.createElement 'canvas'
		$domElement = $ @view.get 'domElement'
		$domElement.append @canvas
		@$canvas = $ @canvas
		@$canvas.addClass 'hud'
		@font = 'Arial'
		@resize()
		
	resize: ->
		@canvas.width = @$canvas.parent().width()
		@canvas.height = @$canvas.parent().height()
		#console.log 'Set hud canvas size', @canvas.width, @canvas.height
		@initCtx()
		@update()

	initCtx: ->
		@ctx = @canvas.getContext '2d'
		@ctx.textAlign = 'center'
		@ctx.textBaseline = 'top'

	update: (delta, now) ->
		@ctx.clearRect 0, 0, @canvas.width, @canvas.height
		@elements.forEach (element) =>
			element.update delta, now
			@elements.splice @elements.indexOf(element), 1 unless element.active

	_drawRect: (opts) ->
		opts = _.defaults opts, fillStyle: 'black', lineWidth: 0, strokeStyle: 'black' 
		@ctx.beginPath()
		@ctx.rect opts.size.x, opts.size.y, opts.size.w, opts.size.h
		@ctx[key] = value for key, value of _.omit opts, 'size'
		@ctx.fill()
		@ctx.stroke() if opts.lineWidth
	
	_drawLinearGradient: (opts) ->
		lingrad = @ctx.createLinearGradient 0, 0, 0, opts.size.h
		lingrad.addColorStop stop.stop, stop.color for stop in opts.stops
		@ctx.beginPath()
		@ctx.fillStyle = lingrad
		@ctx.fillRect opts.size.x, opts.size.y, opts.size.w, opts.size.h
	
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

return BaseHUD