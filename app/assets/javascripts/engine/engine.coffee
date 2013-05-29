Variables = require 'variables'
scenegraph = require 'scenegraph'
Eventbus = require 'eventbus'
events = require 'events'

_initialized = false

engine = 

	initialize: ->
		unless _initialized
			_initialized = true
			console.log 'Engine starting...'
			Views = require 'core/views'
			@views = new Views()
			viewport = $ @views.viewports.at(0).get 'domElement'
			position = viewport.position()
			Variables.SCREEN_TOP = position.top
			Variables.SCREEN_LEFT = position.left
			Variables.SCREEN_WIDTH = viewport.width()
			Variables.SCREEN_HEIGHT = viewport.height()
			Controls = require 'core/controls'
			@controls = new Controls()
			@clock = new THREE.Clock()
			@pause = false
			scenegraph.initialize()
			console.log "Engine started!"
		return
		
	start: ->
		@initialize()
		console.log "Starting main loop..."
		_animate()
		return
	
	stop: ->
		engine.pause = true
		return

	render: ->
		delta = @clock.getDelta()
		scenegraph.update delta
		@views.render delta
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