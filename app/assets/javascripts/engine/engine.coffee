Variables = require 'variables'
SceneGraph = require 'scenegraph'
ViewHandler = require 'handler/viewhandler'
Eventbus = require 'eventbus'

class Engine 

	constructor: (@app) ->
		throw 'No View declared' unless @app.views
		
		
	init: ->
		console.log "Engine starting..."
		@main = $ '#main'
		@canvas = document.createElement 'canvas'
		Variables.SCREEN_WIDTH = @main.width()
		Variables.SCREEN_HEIGHT = @main.height()
		@renderer = @initRenderer()
		@scenegraph = new SceneGraph(@)
		@viewhandler = new ViewHandler(@, @app.views)
		@mouseX = 0
		@mouseY = 0
		@scenegraph.init()
		#document.addEventListener 'mousemove', @onDocumentMouseMove, false
		@initListener()
		console.log "Engine started!"
		
	initRenderer: ->
		renderer = new THREE.WebGLRenderer 
			antialias: true
		
		renderer.setSize Variables.SCREEN_WIDTH,Variables.SCREEN_HEIGHT
		renderer.domElement.style.position = "relative"

		@main.append renderer.domElement
		renderer
		
	initListener:  ->
		Eventbus.cameraChanged.add(@cameraChanged)
		Eventbus.windowResize.add(@onWindowResize)
		
	start: ->
		if (@main == undefined)
			@init()
		console.log "Starting main loop..."
		@animate()
	
	render: ->
		@scenegraph.update()
		@viewhandler.render(@renderer, @scenegraph.scene, @mouseX, @mouseY)
		

	animate: =>
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

	cameraChanged: (camera) =>
		console.log 'Rendering engine camera event ... '
		@viewhandler.render(@renderer, @scenegraph.scene, @mouseX, @mouseY)
		
return Engine