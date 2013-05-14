EngineRenderer = require 'enginerenderer'
Variables = require 'variables'
SceneGraph = require 'scenegraph'
Eventbus = require 'eventbus'
Views = require 'core/views'
events = require 'events'

class Engine 

	constructor: (@opts) ->
		@opts = @opts || {}
		@rendererType = Variables.RENDERER_TYPE_WEBGL unless @opts.rendererType
		@main = @opts.container
		@main = $ '#main' unless @main
		@data = @opts.data || {}

	init: ->
		console.log 'Engine starting...'
		position = @main.position()
		Variables.SCREEN_TOP = position.top
		Variables.SCREEN_LEFT = position.left
		Variables.SCREEN_WIDTH = @main.width()
		Variables.SCREEN_HEIGHT = @main.height()
		@renderer = @initRenderer()
		@scenegraph = new SceneGraph @
		@views = new Views @
		@clock = new THREE.Clock()
		@pause = false
		@initListener()
		console.log "Engine started!"
		return
		
	initRenderer: ->
		if @rendererType is Variables.RENDERER_TYPE_CANVAS
			renderer = new THREE.CanvasRenderer
				clearColor: 0xffffff
		else
			renderer = new EngineRenderer 
				antialias: true
			renderer.autoClear = false
		renderer.setSize Variables.SCREEN_WIDTH,Variables.SCREEN_HEIGHT
		@main.append renderer.domElement
		renderer
		
	initListener:  ->
		Eventbus.cameraChanged.add @onCameraChanged
		Eventbus.windowResize.add @onWindowResize
		return
		
	start: ->
		if (@main == undefined)
			@init()
		console.log "Starting main loop..."
		@animate()
		return

	shutdown: ->
		$(@renderer.domElement).remove()
		return

	render: ->
		delta = @clock.getDelta()
		@scenegraph.update delta
		events.trigger 'engine:render:before', delta
		Eventbus.beforeRender.dispatch()
		@views.render @renderer, @rendererType, @scenegraph.scene, @scenegraph.skyboxScene
		events.trigger 'engine:render:after', delta
		Eventbus.afterRender.dispatch()
		return
		
	animate: =>
		unless @pause
			@render()
			requestAnimationFrame(@animate)
		else
			console.log 'Main Loop stopped ...'
		return
	
	onDocumentMouseMove : (event) =>
		@mouseX = event.clientX - Variables.SCREEN_WIDTH / 2 
		@mouseY = event.clientY - Variables.SCREEN_HEIGHT / 2 
		return
		
	onWindowResize: (withReRender) =>
		Variables.SCREEN_WIDTH = @main.width()
		Variables.SCREEN_HEIGHT = @main.height()
		@renderer.setSize Variables.SCREEN_WIDTH,Variables.SCREEN_HEIGHT
		@views.resizeViews()
		if (withReRender) 
			@render()
		return

	onCameraChanged: (view) =>
		@views.cameraRender @renderer, @rendererType, @scenegraph.scene, @scenegraph.skyboxScene, view
		return

	getData: (type, name) ->
		throw "Data of type #{type} with name #{name} not found" unless type of @data and name of @data[type]
		@data[type][name]

return Engine