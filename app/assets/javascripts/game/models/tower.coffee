AnimatedModel = require 'models/animatedModel'

class Tower extends AnimatedModel
	
	active: false
	
	update: (delta) ->
		super delta if @active

return Tower