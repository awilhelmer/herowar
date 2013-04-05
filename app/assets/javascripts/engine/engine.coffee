Variables = require 'variables'
SceneGraph = require 'scenegraph'
ViewHandler = require 'handler/viewhandler'
Eventbus = require 'eventbus'

class Engine 

	constructor: (@app, @rendererType) ->
		throw 'No View declared' unless @app.views

	init: ->
		console.log 'Engine starting...'
		@rendererType = Variables.RENDERER_TYPE_WEBGL unless @rendererType
		@main = $ '#main'
		@canvas = document.createElement 'canvas'
		Variables.SCREEN_WIDTH = @main.width()
		Variables.SCREEN_HEIGHT = @main.height()
		@renderer = @initRenderer()
		@scenegraph = new SceneGraph(@)
		@viewhandler = new ViewHandler(@, @app.views)
		@pause = false
		@data = {}
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