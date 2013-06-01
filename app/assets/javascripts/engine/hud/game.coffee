scenegraph = require 'scenegraph'
BaseHUD = require 'hud/baseHud'
db = require 'database'

class GameHUD extends BaseHUD
	
	initialize: ->
		@projector = new THREE.Projector()
		@initalized = false
		@waves = db.get 'ui/waves'
		@state = 0

	update: (delta) ->
		return unless @_gameIsInitialized()
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
				@_drawText "GAME START", screen_hw, screen_hh / 2, ((1 - @alpha) * width * 4) + width * 2
				@alpha = @alpha - delta if delta
				@state++ if @alpha <= 0
		@_drawHealthBars()
		
	_drawHealthBars: ->
		viewportWidthHalf = @canvas.width / 2
		viewportHeightHalf = @canvas.height / 2
		@_setShadow 0, 0, 0
		for id, obj of scenegraph.getDynObjects() when obj.showHealth and not obj.isDead()
			@_drawHealthBar obj, viewportWidthHalf, viewportHeightHalf

	_drawHealthBar: (obj, viewportWidthHalf, viewportHeightHalf) ->
		boundaryBox = obj.meshBody.geometry.boundingBox
		position = obj.getMainObject().position.clone()
		position.x -= Math.abs(boundaryBox.min.x) * obj.meshBody.scale.x
		position.y += (boundaryBox.max.y - boundaryBox.min.y) * obj.meshBody.scale.y
		@projector.projectVector position, @view.get 'cameraScene'
		position.x = (position.x * viewportWidthHalf) + viewportWidthHalf
		position.y = - (position.y * viewportHeightHalf) + viewportHeightHalf
		percent = obj.currentHealth / obj.maxHealth
		width = Math.round @canvas.height / 10
		height = Math.round width / 7.5
		@_drawRect 
			size: 
				x: position.x
				y: position.y
				w: width
				h: height
			fillStyle: 'rgba(0, 0, 0, 0.7)'
			lineWidth: 1
			strokeStyle: 'black'
		@_drawLinearGradient 
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

	_gameIsInitialized: ->
		@initalized = @waves and @waves.get '_active' unless @initalized
		return @initalized
	
	_gameIsStarted: ->
		return @waves.get('current') isnt 0

return GameHUD