BaseEmitterEffect = require 'effects/baseEmitterEffect'
db = require 'database'

class BlackSmoke extends BaseEmitterEffect
	
	getTexture: ->
		return db.data()['textures']['cloud10']

	update: (delta, now) ->
		super delta, now
		@start() if not @run and @owner.getHealthPercentage() <= 50
		@stop() if @run and @owner.currentHealth is 0
		return

return BlackSmoke