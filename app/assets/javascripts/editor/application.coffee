Engine = require 'engine'
Variables = require 'variables'
EditorBindings = require 'ui/bindings'
EditorScenegraph = require 'ui/panel/scenegraph'
Eventbus = require 'eventbus'

app =
	
	views : [
		left: 0,
		bottom: 0,
		width: 1.0,
		height: 1.0,
		background: { r: 0, g: 0, b: 0, a: 1 },
		eye: [ 0, 0, 2000 ],
		up: [ 1, 1, 1 ],
		fov: 75,
		type: Variables.CAMERA_TYPE_FREE		
		]
		
	start: ->
		app.engine = new Engine(app)
		app.engine.init()
		app.render()
		app.editorBindings = new EditorBindings(app)
		app.editorBindings.init()
		app.editorScenegraph = new EditorScenegraph(app)
		window.addEventListener 'resize',  ->
			Eventbus.windowResize.dispatch true
			null
		, false 
		
		#All listeners must do a reRender!
		mousepressed = false
		
		app.engine.main.get(0).addEventListener 'mouseup', (event) -> 
			console.log 'mouseup'
			app.controlsChanged(event)
			app.mousepressed = false
			null
		, false 
		app.engine.main.get(0).addEventListener 'mousedown',(event) ->
			console.log 'mousedown'
			app.controlsChanged(event)
			app.mousepressed = true
			null
		, false 
		app.engine.main.get(0).addEventListener 'mousemove',(event) ->
			if (app.mousepressed)
				app.controlsChanged(event)
			null
		, false 
		app.engine.main.get(0).addEventListener 'mousewheel', @controlsChanged, false 
		app.engine.main.get(0).addEventListener 'DOMMouseScroll', @controlsChanged, false 
		app.engine.main.get(0).addEventListener 'touchstart', @controlsChanged, false 
		app.engine.main.get(0).addEventListener 'touchend', @controlsChanged, false 
		app.engine.main.get(0).addEventListener 'touchmove', @controlsChanged, false
		window.addEventListener( 'keydown', @controlsChanged, false );
		window.addEventListener( 'keyup', @controlsChanged, false );
		#End of listeners

	renderer: ->
		app.engine.renderer

	scenegraph: ->
		app.engine.scenegraph
	
	render: ->
		app.engine.render()
	
	controlsChanged: (event) =>
		Eventbus.controlsChanged.dispatch event
		null
    
return app
