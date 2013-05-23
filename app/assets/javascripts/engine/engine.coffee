Variables = require 'variables'
sceneGraph = require 'scenegraph'
Eventbus = require 'eventbus'
Controls = require 'core/controls'
Views = require 'core/views'
events = require 'events'

engine = 

	init: ->
		console.log 'Engine starting...'
		viewport = $ '#viewport'
		position = viewport.position()
		Variables.SCREEN_TOP = position.top
		Variables.SCREEN_LEFT = position.left
		Variables.SCREEN_WIDTH = viewport.width()
		Variables.SCREEN_HEIGHT = viewport.height()
		@scenegraph = sceneGraph
		@scenegraph.initialize()
		@views = new Views()
		@controls = new Controls()
		@clock = new THREE.Clock()
		@pause = false
		console.log "Engine started!"
		return
		
	start: ->
		@init()
		console.log "Starting main loop..."
		_animate()
		return

	render: ->
		delta = @clock.getDelta()
		@scenegraph.update delta
		@views.render @scenegraph.scene, @scenegraph.skyboxScene
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