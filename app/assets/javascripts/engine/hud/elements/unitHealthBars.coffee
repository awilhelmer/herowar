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
			@_drawHealthBar obj, viewportWidthHalf, viewportHeightHalf
		return
		
	_drawHealthBar: (obj, viewportWidthHalf, viewportHeightHalf) ->
		position = objectUtils.positionToScreen obj, viewportWidthHalf, viewportHeightHalf, @view.get 'cameraScene'
		percent = obj.currentHealth / obj.maxHealth
		width = Math.round @canvas.height / 10
		height = Math.round width / 7.5
		canvasUtils.drawRect @ctx,
			size: 
				x: position.x
				y: position.y
				w: width
				h: height
			fillStyle: 'rgba(0, 0, 0, 0.7)'
			lineWidth: 1
			strokeStyle: 'black'
		canvasUtils.drawLinearGradient @ctx,
			size: 
				x: position.x + 1
				y: position.y + 1
				w: percent * (width - 2)
				h: height - 2
			stops: [ 
				stop: 0, color: '#f85032'
				stop: 0.5, color: '#f16f5c'
				stop: 0.51, color: '#f6290c'
				stop: 0.71, color: '#f02f17'
				stop: 1, color: '#e73827'
			]
		return
	
return UnitHealthBarsHUDElement