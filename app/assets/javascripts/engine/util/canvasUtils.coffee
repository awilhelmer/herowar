__measuretext_cache__ = {}

canvasUtils =

	font: 'Arial'

	drawText: (ctx, content, x, y, width) ->
		baseFontSize = 24
		size = @_measureText "#{content}", @font, baseFontSize, false
		newFontSize = baseFontSize * (width / size[0])
		ctx.font = "#{newFontSize}px #{@font}"
		ctx.fillText "#{content}", x, y

	drawMultilineBlockText: (ctx, content, x, y, width) ->
		splittedLines = content.split '\n'
		baseFontSize = 24
		addToY = 0
		for line in splittedLines
			size = @_measureText "#{line}", @font, baseFontSize, false
			newFontSize = baseFontSize * (width / size[0])
			ctx.font = "#{newFontSize}px #{@font}"
			ctx.fillText "#{line}", x, y + addToY
			size = @_measureText "#{line}", @font, newFontSize, false
			addToY += size[1]

	drawRect: (ctx, opts) ->
		opts = _.defaults opts, fillStyle: 'black', lineWidth: 0, strokeStyle: 'black' 
		ctx.beginPath()
		ctx.rect opts.size.x, opts.size.y, opts.size.w, opts.size.h
		ctx[key] = value for key, value of _.omit opts, 'size'
		ctx.fill()
		ctx.stroke() if opts.lineWidth
	
	drawLinearGradient: (ctx, opts) ->
		lingrad = ctx.createLinearGradient 0, 0, 0, opts.size.h
		lingrad.addColorStop stop.stop, stop.color for stop in opts.stops
		ctx.beginPath()
		ctx.fillStyle = lingrad
		ctx.fillRect opts.size.x, opts.size.y, opts.size.w, opts.size.h

	setShadow: (ctx, shadowOffsetX, shadowOffsetY, shadowBlur) ->
		ctx.shadowOffsetX = shadowOffsetX
		ctx.shadowOffsetY = shadowOffsetY
		ctx.shadowBlur = shadowBlur
		ctx.shadowColor = 'rgba(0, 0, 0, 0.5)'

	_measureText: (text, font, size, bold) ->
		key = "#{text}:#{font}:#{size}:#{bold}"
		return __measuretext_cache__[key] if _.has __measuretext_cache__, key
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
		__measuretext_cache__[key] = size
		return size

return canvasUtils