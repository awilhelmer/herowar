MeshModel = require 'models/mesh'

class AnimatedModel extends MeshModel

	activeAnimation = null
	
	animationFPS: 6	

	constructor: (@id, @name, @meshBody) ->
		super @id, @name, @meshBody
		@setAnimation @meshBody.geometry.firstAnimation if @meshBody.geometry.firstAnimation

	update: (delta) ->
		@animate delta

	animate: (delta) ->
		@meshBody.updateAnimation 1000 * delta	if @meshBody and @activeAnimation
	
	setAnimation: (name) ->
		console.log "Model #{@name}-#{@id} set animation to #{name}"
		if @meshBody
			@meshBody.playAnimation name, @animationFPS 
			@meshBody.baseDuration = @meshBody.duration
		@activeAnimation = name

return AnimatedModel