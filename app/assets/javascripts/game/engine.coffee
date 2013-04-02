Variables = require 'variables'

###
Engine is the main entry to initialize the game
	@author Alexander Wilhelmer
###

class Engine 
	

	start: ->
		console.log "Engine starting..."
		renderer: @initRenderer
		console.log "Engine started!"
		
		
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
		
return Engine