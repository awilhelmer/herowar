events = require 'events'

class BaseHUD
	
	default: []
	
	constructor: (@view) ->
		@elements = []
		@bindEvents()
		@createCanvas()
		@initialize()
	
	bindEvents: ->
		events.on 'engine:render', @update, @
		events.on 'scene:terrain:build', @terrainBuild, @
		return

	initialize: ->

	createCanvas: ->
		@canvas = document.createElement 'canvas'
		$domElement = $ @view.get 'domElement'
		$domElement.append @canvas
		@$canvas = $ @canvas
		@$canvas.addClass 'hud'
		@font = 'Arial'
		@ctx = @canvas.getContext '2d'
		@resize()
		return
		
	resize: ->
		@canvas.width = @$canvas.parent().width()
		@canvas.height = @$canvas.parent().height()
		return

	update: (delta, now) ->
		@ctx.clearRect 0, 0, @canvas.width, @canvas.height
		@ctx.textAlign = 'center'
		@ctx.textBaseline = 'top'
		@elements.forEach (element) =>
			if element.active
				element.update delta, now
			else
				@elements.splice @elements.indexOf(element), 1 unless element.active
		return

	terrainBuild: (map) ->
		@elements.push new (require val) @canvas, @view  for val in @default
		return

return BaseHUD