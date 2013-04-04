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
		up: [ 0, 0, 1 ],
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
		
	scenegraph: ->
		app.engine.scenegraph
	
	render: ->
		app.engine.render()
    
return app
