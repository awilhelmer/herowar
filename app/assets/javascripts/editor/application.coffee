Engine = require 'engine'
Variables = require 'variables'
Preloader = require 'preloader'
Eventbus = require 'eventbus'
Editor = require 'editor'

app =
	
	views : [
		left: 0,
		bottom: 0,
		width: 1.0,
		height: 1.0,
		background: { r: 0, g: 0, b: 0, a: 1 },
		eye: [ 300, 150, 300 ],
		up: [ 1, 1, 1 ],
		fov: 75,
		type: Variables.CAMERA_TYPE_FREE		
	]
		
	start: ->
		@createEngine app, Variables.RENDERER_TYPE_CANVAS
		@preload()
	
	preload: ->
		Eventbus.preloadComplete.add @onPreloadComplete
		app.preloader = new Preloader(app)
		app.preloader.init
			texturesCube:
				'default' : 'assets/images/game/skybox/default/%1.jpg'
		app.engine.animate()

	onPreloadComplete: (app) =>
		Eventbus.preloadComplete.remove app.onPreloadComplete
		app.engine.shutdown()
		app.createEngine app, Variables.RENDERER_TYPE_WEBGL
		app.engine.data = app.preloader.data
		app.loadEditor(app)
		
	loadEditor: (app) ->	
		app.editor = new Editor app
		app.editor.init()
	
	createEngine: (app, type) =>
		app.engine = new Engine app, type
		app.engine.init()
    
return app
