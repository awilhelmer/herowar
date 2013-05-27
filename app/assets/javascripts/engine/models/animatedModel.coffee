MeshModel = require 'models/mesh'

class AnimatedModel extends MeshModel

	activeAnimation: null
	
	playOnce: false
	
	animationFPS: 6	

	constructor: (@id, @name, @meshBody) ->
		super @id, @name, @meshBody
		@setAnimation @meshBody.geometry.firstAnimation if @meshBody.geometry.firstAnimation

	update: (delta) ->
		super delta
		@animate delta

	animate: (delta) ->
		if @meshBody and @activeAnimation
			if @playOnce and @meshBody.currentKeyframe is @meshBody.endKeyframe
				@activeAnimation = null
			else 
				@meshBody.updateAnimation 1000 * delta			
	
	setAnimation: (name, playOnce) ->
		if _.isBoolean playOnce then @playOnce = playOnce else @playOnce = false
		if @meshBody
			@meshBody.playAnimation name, @animationFPS 
			@meshBody.baseDuration = @meshBody.duration
		@activeAnimation = name

return AnimatedModel