Variables = require 'variables'
SceneGraph = require 'scenegraph'

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
		
		@initCameras()
		@mouseX = 0
		@mouseY = 0
		@scenegraph.init()
		#document.addEventListener 'mousemove', @onDocumentMouseMove, false
		window.addEventListener 'resize', @onWindowResize, false 
		console.log "Engine started!"
		
	initRenderer: ->
		renderer = new THREE.WebGLRenderer 
			antialias: true
		
		renderer.setSize Variables.SCREEN_WIDTH,Variables.SCREEN_HEIGHT
		renderer.domElement.style.position = "relative"

		@main.append renderer.domElement
		renderer
		
	initCameras: ->
		for view in @app.views
			view.camera = new THREE.PerspectiveCamera view.fov, @renderer.domElement.innerWidth / @renderer.domElement.innerHeight, 1, 10000
			view.camera.position.x = view.eye[ 0 ]
			view.camera.position.y = view.eye[ 1 ]
			view.camera.position.z = view.eye[ 2 ]
			view.camera.up.x = view.up[ 0 ]
			view.camera.up.y = view.up[ 1 ]
			view.camera.up.z = view.up[ 2 ]

	start: ->
		if (@main == undefined)
			@init()
		console.log "Starting main loop..."
		@animate()
	
	render: ->
		for view in @app.views
				view.updateCamera( view.camera, @scenegraph.scene, @mouseX, @mouseY );
				left = Math.floor Variables.SCREEN_WIDTH * view.left 
				bottom = Math.floor Variables.SCREEN_HEIGHT * view.bottom
				width = Math.floor Variables.SCREEN_WIDTH * view.width 
				height = Math.floor Variables.SCREEN_HEIGHT * view.height 
				@renderer.setViewport left, bottom, width, height
				@renderer.setScissor left, bottom, width, height 
				@renderer.enableScissorTest  true 
				@renderer.setClearColor view.background, view.background.a 
				view.camera.aspect = width / height
				view.camera.updateProjectionMatrix()
				@renderer.render(@scenegraph.scene, view.camera)
		null
		
	animate: =>
		@scenegraph.update()
		@render()
		requestAnimationFrame(@animate)
	
	onDocumentMouseMove : (event) =>
		@mouseX = event.clientX - Variables.SCREEN_WIDTH / 2 
		@mouseY = event.clientY - Variables.SCREEN_HEIGHT / 2 
		null
		
	onWindowResize: () =>
		Variables.SCREEN_WIDTH = @main.width()
		Variables.SCREEN_HEIGHT = @main.height()
		@renderer.setSize Variables.SCREEN_WIDTH,Variables.SCREEN_HEIGHT
		null
		
return Engine