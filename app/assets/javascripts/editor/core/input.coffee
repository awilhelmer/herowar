EditorEventbus = require 'editorEventbus'
Eventbus = require 'eventbus'
Variables = require 'variables'

class Input

	constructor: (@editor) ->
		@initialize()
	
	initialize: ->
		@addEventListener()
	
	addEventListener: ->
		console.log 'Register input listeners'
		window.addEventListener 'resize',  => Eventbus.windowResize.dispatch true
		window.addEventListener 'keyup', @onKeyUp event
		window.addEventListener 'keydown', @onKeyDown event
		@editor.engine.main.get(0).addEventListener 'mouseup', (event) => @onMouseUp event
		@editor.engine.main.get(0).addEventListener 'mousedown', (event) => @onMouseDown event
		@editor.engine.main.get(0).addEventListener 'mousemove',(event) => @onMouseMove event
		@editor.engine.main.get(0).addEventListener 'mousewheel', (event) => @onMouseWheel event
		@editor.engine.main.get(0).addEventListener 'DOMMouseScroll', @onDOMMouseScroll event
		@editor.engine.main.get(0).addEventListener 'touchstart', @onTouchStart event
		@editor.engine.main.get(0).addEventListener 'touchend', @onTouchEnd event
		@editor.engine.main.get(0).addEventListener 'touchmove', @onTouchMove event

	onKeyUp: (event) ->
		EditorEventbus.keyup.dispatch event
		Eventbus.controlsChanged.dispatch event

	onKeyDown: (event) ->
		EditorEventbus.keydown.dispatch event
		Eventbus.controlsChanged.dispatch event

	onMouseUp: (event) ->
		console.log 'mouseup'
		if event
			Variables.MOUSE_PRESSED_LEFT = false if event.which is 1
			Variables.MOUSE_PRESSED_MIDDLE = false if event.which is 2
			Variables.MOUSE_PRESSED_RIGHT = false if event.which is 3
		EditorEventbus.mouseup.dispatch event
		Eventbus.controlsChanged.dispatch event
		Variables.MOUSE_MOVED = false unless Variables.MOUSE_PRESSED_LEFT or Variables.MOUSE_PRESSED_MIDDLE or Variables.MOUSE_PRESSED_RIGHT
		###
		if event?.button is 0 and !@mouseMoved and @tool is Constants.TOOL_SELECTION
			@selectorObject.update()
		@dispatchControlsChangedEvent event
		@camera.update()
		@mousePressed = false
		@mouseMoved = false
		###
		
	onMouseDown: (event) ->
		console.log 'mousedown'
		if event
			Variables.MOUSE_PRESSED_LEFT = true if event.which is 1
			Variables.MOUSE_PRESSED_MIDDLE = true if event.which is 2
			Variables.MOUSE_PRESSED_RIGHT = true if event.which is 3
		EditorEventbus.mousedown.dispatch event
		Eventbus.controlsChanged.dispatch event
		Variables.MOUSE_MOVED = false
		###
		@dispatchControlsChangedEvent event
		@camera.update()
		@mousePressed = true
		@mouseMoved = false
		###
		
	onMouseMove: (event) ->
		console.log 'mousemove'
		if event
			Variables.MOUSE_POSITION_X = event.clientX
			Variables.MOUSE_POSITION_Y = event.clientY
		EditorEventbus.mousemove.dispatch event
		Eventbus.controlsChanged.dispatch event if Variables.MOUSE_PRESSED_LEFT
		Variables.MOUSE_MOVED = true
		###
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
		###
	
	onMouseWheel: (event) ->
		EditorEventbus.mousewheel.dispatch event
		Eventbus.controlsChanged.dispatch event

		
	onDOMMouseScroll: (event) ->
		EditorEventbus.domMouseScroll.dispatch event
		Eventbus.controlsChanged.dispatch event

	onTouchStart: (event) ->
		EditorEventbus.touchstart.dispatch event
		Eventbus.controlsChanged.dispatch event

	onTouchEnd: (event) ->
		EditorEventbus.touchend.dispatch event
		Eventbus.controlsChanged.dispatch event

	onTouchMove: (event) ->
		EditorEventbus.touchmove.dispatch event
		Eventbus.controlsChanged.dispatch event	

return Input