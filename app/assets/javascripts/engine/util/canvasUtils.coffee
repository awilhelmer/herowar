__measuretext_cache__ = {}

canvasUtils =

	font: 'Arial'

	drawText: (ctx, content, x, y, width) ->
		baseFontSize = 24
		size = @_measureText "#{content}", @font, baseFontSize, false
		newFontSize = baseFontSize * (width / size[0])
		ctx.font = "#{newFontSize}px #{@font}"
		ctx.fillText "#{content}", x, y
		return

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
		return

	drawRect: (ctx, opts) ->
		opts = _.defaults opts, fillStyle: 'black', lineWidth: 0, strokeStyle: 'black' 
		ctx.beginPath()
		ctx.rect opts.size.x, opts.size.y, opts.size.w, opts.size.h
		ctx[key] = value for key, value of _.omit opts, 'size'
		ctx.fill()
		ctx.stroke() if opts.lineWidth
		return
	
	drawBubble: (ctx, opts) ->
		opts = _.defaults opts, radius: 10, position: 'top-left'
		r = opts.x + opts.w
		b = opts.y + opts.h
		ctx.moveTo opts.x + opts.radius, opts.y
		if opts.position is 'top-left'
			ctx.lineTo opts.x + opts.radius / 2, opts.y - opts.radius
			ctx.lineTo opts.x + opts.radius * 2, opts.y
		if opts.position is 'top'
			ctx.lineTo opts.x + opts.w / 2 - opts.radius / 2, opts.y
			ctx.lineTo opts.x + opts.w / 2, opts.y - opts.radius
			ctx.lineTo opts.x + opts.w / 2 + opts.radius / 2, opts.y
		if opts.position is 'top-right'
			ctx.lineTo r - opts.radius * 2, opts.y
			ctx.lineTo r - opts.radius / 2, opts.y - opts.radius
		ctx.lineTo r - opts.radius, opts.y
		ctx.quadraticCurveTo r, opts.y, r, opts.y + opts.radius
		if opts.position is 'right'
			ctx.lineTo r, opts.y + opts.h / 2 - opts.radius / 2
			ctx.lineTo r + opts.radius, opts.y + opts.h / 2
			ctx.lineTo r, opts.y + opts.h / 2 + opts.radius / 2
		ctx.lineTo r, opts.y + opts.h - opts.radius
		ctx.quadraticCurveTo r, b, r - opts.radius, b
		if opts.position is 'bottom-right'
			ctx.lineTo r - opts.radius / 2, b + opts.radius
			ctx.lineTo r - opts.radius * 2, b
		if opts.position is 'bottom'
			ctx.lineTo opts.x + opts.w / 2 + opts.radius / 2, b
			ctx.lineTo opts.x + opts.w / 2, b + opts.radius
			ctx.lineTo opts.x + opts.w / 2 - opts.radius / 2, b
		if opts.position is 'bottom-left'
			ctx.lineTo opts.x + opts.radius * 2, b
			ctx.lineTo opts.x + opts.radius / 2, b + opts.radius
		ctx.lineTo opts.x + opts.radius, b
		ctx.quadraticCurveTo opts.x, b, opts.x, b - opts.radius
		if opts.position is 'left'
			ctx.lineTo opts.x, opts.y + opts.h / 2 + opts.radius / 2
			ctx.lineTo opts.x - opts.radius, opts.y + opts.h / 2
			ctx.lineTo opts.x, opts.y + opts.h / 2 - opts.radius / 2
		ctx.lineTo opts.x, opts.y + opts.radius
		ctx.quadraticCurveTo opts.x, opts.y, opts.x + opts.radius, opts.y
		return
	
	drawLinearGradient: (ctx, opts) ->
		lingrad = ctx.createLinearGradient 0, 0, 0, opts.size.h
		lingrad.addColorStop stop.stop, stop.color for stop in opts.stops
		ctx.beginPath()
		ctx.fillStyle = lingrad
		ctx.fillRect opts.size.x, opts.size.y, opts.size.w, opts.size.h
		return

	setShadow: (ctx, shadowOffsetX, shadowOffsetY, shadowBlur) ->
		ctx.shadowOffsetX = shadowOffsetX
		ctx.shadowOffsetY = shadowOffsetY
		ctx.shadowBlur = shadowBlur
		ctx.shadowColor = 'rgba(0, 0, 0, 0.5)'
		return

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