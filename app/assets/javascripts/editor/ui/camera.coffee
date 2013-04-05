class Camera

	constructor: (@app) ->
		@$container = $ '#camera'
		@container = @$container[0]
		@init()
		
	init: ->
		@update()
		
	update: ->
		position = @app.engine.viewhandler.views[0].camera.position
		@$container.html("<b>Camera</b> X: #{Math.round(position.x)} / Y: #{Math.round(position.y)} / Z: #{Math.round(position.z)}")

return Camera