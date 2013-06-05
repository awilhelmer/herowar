textureUtils =
	
	buildCanvasFromImage: (opts) ->
		image = opts.image
		alpha = opts.alpha || false
		canvas = document.createElement 'canvas'
		canvas.width = image.width
		canvas.height = image.height
		ctx = canvas.getContext '2d'
		ctx.drawImage image, 0, 0
		if alpha
			computeAlpha = @_computeAlphaFromLuminance 16, 1
			imgData	= ctx.getImageData 0, 0, canvas.width, canvas.height
			i = 0
			for y in [0..canvas.height-1]
				for x in [0..canvas.width-1]
					alpha	= computeAlpha imgData.data, i
					imgData.data[i + 3] = alpha
					i += 4
			ctx.putImageData imgData, 0, 0
		return canvas
	
	buildTiledSpriteSheet: (opts) ->
		images = opts.images
		spriteW = opts.spriteW
		spriteH = opts.spriteH
		nSpriteX = opts.nSpriteW || 1
		nSpriteY = opts.nSpriteY || (images.length / nSpriteX)
		canvasW = opts.canvasW || (spriteW * nSpriteX)
		canvasH = opts.canvasH || (spriteH * nSpriteY)
		canvas = document.createElement 'canvas'
		canvas.width = canvasW
		canvas.height = canvasH
		ctx = canvas.getContext '2d'
		for image, idx in images
			destX = spriteW * (idx % nSpriteX)
			destY = spriteH * Math.floor(idx / nSpriteX)
			ctx.drawImage image, 0, 0, image.width, image.height, destX, destY, spriteW, spriteH
		return canvas

	createFromImage: (opts) ->
		canvas = @buildCanvasFromImage opts
		texture	= new THREE.Texture canvas
		texture.needsUpdate	= true
		return texture

	_computeAlphaFromLuminance: (multiplier, power) ->
		return (p, i) ->
			luminance	= (0.2126 * p[i + 0]) + (0.7152 * p[i + 1]) + (0.0722 * p[i + 2])
			return (Math.pow(luminance / 255, power) * 255) * multiplier

return textureUtils