Variables = require 'variables'
scenegraph = require 'scenegraph'
Eventbus = require 'eventbus'
events = require 'events'

engine = 

	initialized: false

	initialize: ->
		unless initialized
			initialized = true
			console.log 'Engine starting...'
			viewport = $ '#viewport'
			position = viewport.position()
			Variables.SCREEN_TOP = position.top
			Variables.SCREEN_LEFT = position.left
			Variables.SCREEN_WIDTH = viewport.width()
			Variables.SCREEN_HEIGHT = viewport.height()
			scenegraph.initialize()
			Views = require 'core/views'
			@views = new Views()
			Controls = require 'core/controls'
			@controls = new Controls()
			@clock = new THREE.Clock()
			@pause = false
			console.log "Engine started!"
		return
		
	start: ->
		@initialize()
		console.log "Starting main loop..."
		_animate()
		return

	render: ->
		delta = @clock.getDelta()
		scenegraph.update delta
		@views.render scenegraph.scene, scenegraph.skyboxScene
		events.trigger 'engine:render', delta
		return
		
_animate = ->
	unless engine.pause
		engine.render()
		requestAnimationFrame(_animate)
	else
		console.log 'Main Loop stopped ...'
	return

return engine