class BaseHUDElement
	
	constructor: (@canvas, @view) ->
		@ctx = @canvas.getContext '2d'
		@active = true
		@initialize()
		
	initialize: ->
		return
		
	update: (delta) ->
		return

	getHalfWidth: ->
		return @canvas.width / 2
		
	getHalfHeight: ->
		return @canvas.height / 2

return BaseHUDElement