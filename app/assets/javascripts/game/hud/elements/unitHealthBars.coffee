BaseHUDElement = require 'hud/elements/baseHudElement'
canvasUtils = require 'util/canvasUtils'
objectUtils = require 'util/objectUtils'
scenegraph = require 'scenegraph'

class UnitHealthBarsHUDElement extends BaseHUDElement
		
	update: (delta, now) ->
		viewportWidthHalf = @canvas.width / 2
		viewportHeightHalf = @canvas.height / 2
		canvasUtils.setShadow @ctx, 0, 0, 0
		for id, obj of scenegraph.getDynObjects() when obj.showHealth and not obj.isDead()
			@_drawInfo obj, viewportWidthHalf, viewportHeightHalf
		return
		
	_drawInfo: (obj, viewportWidthHalf, viewportHeightHalf) ->
		position = objectUtils.positionToScreen obj, viewportWidthHalf, viewportHeightHalf, @view.get 'cameraScene'
		width = Math.round @canvas.height / 10
		height = Math.round width / 7.5
		percent = obj.currentHealth / obj.maxHealth
		@_drawBar position.x, position.y, width, height, percent, [ 
				stop: 0, color: '#f85032'
				stop: 0.5, color: '#f16f5c'
				stop: 0.51, color: '#f6290c'
				stop: 0.71, color: '#f02f17'
				stop: 1, color: '#e73827'
			]
		if obj.maxShield
			percent = obj.currentShield / obj.maxShield
			@_drawBar position.x, position.y + height * 0.8, width, height * 0.8, percent, [ 
					stop: 0, color: '#1E5799'
					stop: 0.5, color: '#2989D8'
					stop: 0.51, color: '#207cca'
					stop: 1, color: '#7db9e8'
				]
		return

	_drawBar: (x, y, w, h, percent, stops) ->
		canvasUtils.drawRect @ctx,
			size: 
				x: x
				y: y
				w: w
				h: h
			fillStyle: 'rgba(0, 0, 0, 0.7)'
			lineWidth: 1
			strokeStyle: 'black'
		canvasUtils.drawLinearGradient @ctx,
			size: 
				x: x + 1
				y: y + 1
				w: percent * (w - 2)
				h: h - 2
			stops: stops
	
return UnitHealthBarsHUDElement