EditorBindings = require 'ui/bindings'
EditorScenegraph = require 'ui/panel/scenegraph'
Camera = require 'ui/camera'
Eventbus = require 'eventbus'
	
class Editor

	constructor: (@app) ->

	init: ->
		@camera = new Camera(@)
		@editorBindings = new EditorBindings(@)
		@editorBindings.init()
		@editorScenegraph = new EditorScenegraph(@)
		@addEventListeners(@)

	addEventListeners: (editor) ->
		window.addEventListener 'resize',  ->
			Eventbus.windowResize.dispatch true
			null
		, false 
		
		#All listeners must do a reRender!
		mousepressed = false
		editor.engine().main.get(0).addEventListener 'mouseup', (event) => 
			console.log 'mouseup'
			editor.controlsChanged(event)
			editor.camera.update()
			editor.engine().mousepressed = false
			null
		, false 
		editor.engine().main.get(0).addEventListener 'mousedown',(event) =>
			console.log 'mousedown'
			editor.controlsChanged(event)
			editor.camera.update()
			editor.engine().mousepressed = true
			null
		, false 
		editor.engine().main.get(0).addEventListener 'mousemove',(event) =>
			if (editor.engine().mousepressed)
				editor.controlsChanged(event)
				editor.camera.update()
			null
		, false 
		editor.engine().main.get(0).addEventListener 'mousewheel', editor.controlsChanged, false 
		editor.engine().main.get(0).addEventListener 'DOMMouseScroll', editor.controlsChanged, false 
		editor.engine().main.get(0).addEventListener 'touchstart', editor.controlsChanged, false 
		editor.engine().main.get(0).addEventListener 'touchend', editor.controlsChanged, false 
		editor.engine().main.get(0).addEventListener 'touchmove', editor.controlsChanged, false
		window.addEventListener 'keydown', @controlsChanged, false
		window.addEventListener 'keyup', @controlsChanged, false
		#End of listeners

	renderer: ->
		@app.engine.renderer

	scenegraph: ->
		@app.engine.scenegraph

	render: ->
		@app.engine.render()

	engine: ->
		@app.engine
	
	controlsChanged: (event) =>
		Eventbus.controlsChanged.dispatch event
		null

return Editor