class BaseEffect
	
	constructor: ->
		@active = true
		
	update: (delta, now) ->

	dispose: ->
		@active = false

return BaseEffect