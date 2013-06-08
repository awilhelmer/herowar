BaseEmitterEffect = require 'effects/baseEmitterEffect'
db = require 'database'

class BlackSmoke extends BaseEmitterEffect
	
	getTexture: ->
		return db.data()['textures']['cloud10']

	update: (delta, now) ->
		super delta, now
		@start() if @owner.getHealthPercentage() <= 70
		@stop() if @owner.currentHealth is 0
		return

	setSpriteScale: (sprite) ->
		scale = 1 - @owner.getHealthPercentage() / 150
		sprite.scale.set(scale,scale,scale).multiplyScalar 0.1 + Math.random() * 0.05
		return

return BlackSmoke