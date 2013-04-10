Variables = require 'variables'
SceneGraph = require 'scenegraph'
ViewHandler = require 'handler/viewhandler'
Eventbus = require 'eventbus'

class Engine 

	constructor: (@opts) ->
		@opts = @opts || {}
		throw 'No View declared' unless @opts.views
		@rendererType = Variables.RENDERER_TYPE_WEBGL unless @opts.rendererType
		@main = @opts.container
		@main = $ '#main' unless @main	# TODO: create container if not exists
		@data = @opts.data || {}

	init: ->
		console.log 'Engine starting...'
		position = @main.position()
		Variables.SCREEN_TOP = position.top
		Variables.SCREEN_LEFT = position.left
		Variables.SCREEN_WIDTH = @main.width()
		Variables.SCREEN_HEIGHT = @main.height()
		@renderer = @initRenderer()
		@scenegraph = new SceneGraph(@)
		@viewhandler = new ViewHandler(@, @opts.views)
		@pause = false
		#document.addEventListener 'mousemove', @onDocumentMouseMove, false
		@initListener()
		console.log "Engine started!"
		
	initRenderer: ->
		if @rendererType is Variables.RENDERER_TYPE_CANVAS
			renderer = new THREE.CanvasRenderer
				clearColor: 0xffffff
		else
			renderer = new THREE.WebGLRenderer 
				antialias: true
			renderer.autoClear = false
		renderer.setSize Variables.SCREEN_WIDTH,Variables.SCREEN_HEIGHT
		@main.append renderer.domElement
		renderer
		
	initListener:  ->
		Eventbus.cameraChanged.add @onCameraChanged
		Eventbus.windowResize.add @onWindowResize
		
	start: ->
		if (@main == undefined)
			@init()
		console.log "Starting main loop..."
		@animate()

	shutdown: ->
		$(@renderer.domElement).remove()

	render: ->
		@scenegraph.update()
		Eventbus.beforeRender.dispatch()
		@viewhandler.render @renderer, @rendererType, @scenegraph.scene, @scenegraph.skyboxScene
		Eventbus.afterRender.dispatch()
		
	animate: =>
		unless @pause
			@render()
			requestAnimationFrame(@animate)
		else
			console.log 'Main Loop stopped ...'
	
	onDocumentMouseMove : (event) =>
		@mouseX = event.clientX - Variables.SCREEN_WIDTH / 2 
		@mouseY = event.clientY - Variables.SCREEN_HEIGHT / 2 
		null
		
	onWindowResize: (withReRender) =>
		Variables.SCREEN_WIDTH = @main.width()
		Variables.SCREEN_HEIGHT = @main.height()
		@renderer.setSize Variables.SCREEN_WIDTH,Variables.SCREEN_HEIGHT
		if (withReRender) 
			@render()
		null

	onCameraChanged: (view) =>
		@viewhandler.cameraRender(@renderer, @rendererType, @scenegraph.scene, @scenegraph.skyboxScene, view)
		null

	getData: (type, name) ->
		throw "Data of type #{type} with name #{name} not found" unless type of @data and name of @data[type]
		@data[type][name]

return Engine