Engine = require 'engine'
Variables = require 'variables'
app =

	views : [
		left: 0,
		bottom: 0,
		width: 1.0,
		height: 1.0,
		background: { r: 0, g: 0, b: 0, a: 1 },
		eye: [ 0, 0, 2000 ],
		up: [ 0, 0, 1 ],
		fov: 75,
		type: Variables.CAMERA_TYPE_RTS
		]
		
	start: ->
		app.engine = new Engine(app)
		app.engine.start()
		window.addEventListener('resize',  ->
					Eventbus.windowResize.dispatch false
					null
				,false) 
        
return app
