EditorBindings = require 'ui/bindings'
EditorScenegraph = require 'ui/panel/scenegraph'
Camera = require 'ui/camera'
Eventbus = require 'eventbus'
	
class Editor

	constructor: (@app) ->

	init: ->
		@mousepressed = false
		@camera = new Camera(@)
		@editorBindings = new EditorBindings(@)
		@editorBindings.init()
		@editorScenegraph = new EditorScenegraph(@)
		@addEventListeners(@)

	addEventListeners: (editor) ->
		window.addEventListener 'resize',  => Eventbus.windowResize.dispatch true
		window.addEventListener 'keydown', editor.dispatchControlsChangedEvent
		window.addEventListener 'keyup', editor.dispatchControlsChangedEvent
		editor.engine().main.get(0).addEventListener 'mouseup', (event) => editor.onMouseUp event, editor
		editor.engine().main.get(0).addEventListener 'mousedown', (event) => editor.onMouseDown event, editor
		editor.engine().main.get(0).addEventListener 'mousemove',(event) => editor.onMouseMove event, editor 
		editor.engine().main.get(0).addEventListener 'mousewheel', editor.dispatchControlsChangedEvent
		editor.engine().main.get(0).addEventListener 'DOMMouseScroll', editor.dispatchControlsChangedEvent
		editor.engine().main.get(0).addEventListener 'touchstart', editor.dispatchControlsChangedEvent
		editor.engine().main.get(0).addEventListener 'touchend', editor.dispatchControlsChangedEvent
		editor.engine().main.get(0).addEventListener 'touchmove', editor.dispatchControlsChangedEvent

	renderer: ->
		@app.engine.renderer

	scenegraph: ->
		@app.engine.scenegraph

	render: ->
		@app.engine.render()

	engine: ->
		@app.engine
	
	onMouseUp: (event, editor) ->
		console.log 'mouseup'
		editor.dispatchControlsChangedEvent event
		editor.camera.update()
		editor.mousepressed = false
	
	onMouseDown: (event, editor) ->
		console.log 'mousedown'
		editor.dispatchControlsChangedEvent event
		editor.camera.update()
		editor.mousepressed = true
		
	onMouseMove: (event, editor) ->
		if editor.mousepressed
			editor.dispatchControlsChangedEvent event
			editor.camera.update()		

	dispatchControlsChangedEvent: (event) ->
		Eventbus.controlsChanged.dispatch event

return Editor