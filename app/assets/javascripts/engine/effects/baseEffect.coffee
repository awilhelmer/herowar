class BaseEffect
	
	constructor: ->
		@active = true
		@done = false
		
	update: (delta, now) ->

	dispose: ->
		@active = false
		@done = true

return BaseEffect