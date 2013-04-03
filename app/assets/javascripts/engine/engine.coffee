Variables = require 'variables'

class Engine 

	constructor: (@app) ->
		throw 'No View declared' unless @app.views
		@init()
		
		
	init: ->
		@renderer = @initRenderer()
		@scene = new THREE.Scene()
		light = new THREE.DirectionalLight 0xffffff 
		light.position.set 0, 0, 1
		@scene.add light
		@initCameras()
		@mouseX = 0
		@mouseY = 0
		document.addEventListener 'mousemove', @onDocumentMouseMove, false 
		
	initRenderer: ->
		renderer = new THREE.WebGLRenderer
			antialias: true,
			clearColor: 0x000000
		renderer.physicallyBasedShading = true
		renderer.gammaInput = true
		renderer.gammaOutput = true

		renderer.shadowMapEnabled = true
		renderer.shadowMapSoft = true
		renderer.autoClear = false
		renderer.sortObjects = false
		renderer.setSize Variables.SCREEN_WIDTH,Variables.SCREEN_HEIGHT
		renderer.domElement.style.position = "relative"

		$('#main').append renderer.domElement
		renderer
		
	initCameras: ->
		for view in @app.views
			view.camera = new THREE.PerspectiveCamera view.fov, @renderer.domElement.innerWidth / @renderer.domElement.innerHeight, 1, 10000
		
	start: ->
		console.log "Engine starting..."
		console.log "Engine started!"
		@animate()
	
	render: ->
		for view in @app.views
				view.updateCamera( view.camera, @scene, @mouseX, @mouseY );
				left = Math.floor Variables.SCREEN_WIDTH * view.left 
				bottom = Math.floor Variables.SCREEN_HEIGHT * view.bottom
				width = Math.floor Variables.SCREEN_WIDTH * view.width 
				height = Math.floor Variables.SCREEN_HEIGHT * view.height 
				@renderer.setViewport left, bottom, width, height
				@renderer.setScissor left, bottom, width, height 
				@renderer.enableScissorTest  true 
				@renderer.setClearColor view.background, view.background.a 
				view.camera.aspect = width / height;
				view.camera.updateProjectionMatrix
				@renderer.render(@scene, view.camera)
		
	
	animate: =>
		@render()
		requestAnimationFrame(@animate)
	
	onDocumentMouseMove : (event) ->
		@mouseX = ( event.clientX - Variables.SCREEN_WIDTH / 2 );
		@mouseY = ( event.clientY - Variables.SCREEN_HEIGHT / 2 );
		
return Engine