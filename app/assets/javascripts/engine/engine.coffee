Variables = require 'variables'

class Engine 

	constructor: (@app) ->
		throw 'No View declared' unless @app.view

	
	start: ->
		console.log "Engine starting..."
		@renderer = @initRenderer()
		@scene = new THREE.Scene()
		@camera = @initCamera()
		console.log "Engine started!"
		@animate()
	
	render: ->
		@renderer.render(@scene, @camera)
		
	
	animate: =>
		@render()
		requestAnimationFrame(animate)
		
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
		
	initCamera: ->
		new THREE.PerspectiveCamera view.fov, @renderer.domElement.innerWidth / @renderer.domElement.innerHeight, 1, 10000
		
return Engine