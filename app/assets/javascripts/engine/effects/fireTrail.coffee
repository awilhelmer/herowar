Explosion = require 'effects/explosion'

class FireTrail extends Explosion
	
		isDone: ->
			return @owner.isDisposed

return FireTrail