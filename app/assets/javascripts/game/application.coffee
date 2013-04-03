Engine = require 'engine'

app =

	views : [
		left: 0,
		bottom: 0,
		width: 1.0,
		height: 1.0,
		background: { r: 0.5, g: 0.5, b: 0.7, a: 1 },
		eye: [ 0, 0, 2000 ],
		up: [ 0, 0, 0 ],
		fov: 75,
		updateCamera: (camera, scene, mouseX, mouseY) ->  
			#camera.position.x += mouseX * 0.05;
			#camera.position.x = Math.max Math.min camera.position.x, 2000, -2000
			#camera.lookAt scene.position 
		]
	start: ->
		app.engine = new Engine(app)
		app.engine.start()
        
return app