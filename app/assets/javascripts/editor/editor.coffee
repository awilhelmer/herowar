EditorBindings = require 'ui/bindings'
EditorScenegraph = require 'ui/panel/scenegraph'
EditorMenubar = require 'ui/menubar'
IntersectHelper = require 'helper/intersectHelper'
ObjectHelper = require 'helper/objectHelper'
SelectorArea = require 'helper/selectorArea'
SelectorObject = require 'helper/selectorObject'
Camera = require 'ui/camera'
Eventbus = require 'eventbus'
Variables = require 'variables'
Constants = require 'constants'
	
class Editor

	constructor: (@app) ->

	init: ->
		@tool = Constants.TOOL_SELECTION
		@mousePressed = false
		@mouseMoved = false
		@intersectHelper = new IntersectHelper @
		@objectHelper = new ObjectHelper @
		@selectorArea = new SelectorArea @
		@selectorObject = new SelectorObject @
		@camera = new Camera @
		@editorBindings = new EditorBindings @
		@editorBindings.init()
		@editorScenegraph = new EditorScenegraph @
		@editorMenubar = new EditorMenubar @
		@addEventListeners @

	addEventListeners: (editor) ->
		window.addEventListener 'resize',  => Eventbus.windowResize.dispatch true
		window.addEventListener 'keydown', editor.dispatchControlsChangedEvent
		window.addEventListener 'keyup', editor.dispatchControlsChangedEvent
		editor.engine().main.get(0).addEventListener 'mouseup', (event) => editor.onMouseUp event
		editor.engine().main.get(0).addEventListener 'mousedown', (event) => editor.onMouseDown event
		editor.engine().main.get(0).addEventListener 'mousemove',(event) => editor.onMouseMove event 
		editor.engine().main.get(0).addEventListener 'mousewheel', editor.dispatchControlsChangedEvent
		editor.engine().main.get(0).addEventListener 'DOMMouseScroll', editor.dispatchControlsChangedEvent
		editor.engine().main.get(0).addEventListener 'touchstart', editor.dispatchControlsChangedEvent
		editor.engine().main.get(0).addEventListener 'touchend', editor.dispatchControlsChangedEvent
		editor.engine().main.get(0).addEventListener 'touchmove', editor.dispatchControlsChangedEvent
	
	onMouseUp: (event) ->
		console.log 'mouseup'
		if event?.button is 0 and !@mouseMoved and @tool is Constants.TOOL_SELECTION
			@selectorObject.update()
		@dispatchControlsChangedEvent event
		@camera.update()
		@mousePressed = false
		@mouseMoved = false
	
	onMouseDown: (event) ->
		console.log 'mousedown'
		@dispatchControlsChangedEvent event
		@camera.update()
		@mousePressed = true
		@mouseMoved = false
		
	onMouseMove: (event) ->
		if event
			Variables.MOUSE_X = event.clientX
			Variables.MOUSE_Y = event.clientY
		if @mousePressed
			console.log 'mousemove'
			@dispatchControlsChangedEvent event
			@camera.update()
			@mouseMoved = true
		else if @tool is Constants.TOOL_BRUSH
			@selectorArea.update()

	dispatchControlsChangedEvent: (event) ->
		Eventbus.controlsChanged.dispatch event

	engine: ->
		@app.engine

	scenegraph: ->
		@app.engine.scenegraph

	renderer: ->
		@app.engine.renderer

	render: ->
		@app.engine.render()

return Editor