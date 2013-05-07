Eventbus = require 'eventbus'
Variables = require 'variables'
log = require 'util/logger'

class Input

	constructor: (@editor) ->
		@initialize()
		@addEventListener()
	
	initialize: ->
	
	addEventListener: ->
		log.debug 'Register input listeners'
		window.addEventListener 'resize',  => Eventbus.windowResize.dispatch true
		window.addEventListener 'keyup', (event) => @onKeyUp event
		window.addEventListener 'keydown', (event) => @onKeyDown event
		@editor.engine.main.get(0).addEventListener 'mouseup', (event) => @onMouseUp event
		@editor.engine.main.get(0).addEventListener 'mousedown', (event) => @onMouseDown event
		@editor.engine.main.get(0).addEventListener 'mousemove',(event) => @onMouseMove event
		@editor.engine.main.get(0).addEventListener 'mousewheel', (event) => @onMouseWheel event
		@editor.engine.main.get(0).addEventListener 'DOMMouseScroll', (event) => @onDOMMouseScroll event
		@editor.engine.main.get(0).addEventListener 'touchstart', (event) => @onTouchStart event
		@editor.engine.main.get(0).addEventListener 'touchend', (event) => @onTouchEnd event
		@editor.engine.main.get(0).addEventListener 'touchmove', (event) => @onTouchMove event

	onKeyUp: (event) ->
		Eventbus.controlsChanged.dispatch event

	onKeyDown: (event) ->
		Eventbus.controlsChanged.dispatch event 
		
	onMouseUp: (event) ->
		if event
			Variables.MOUSE_PRESSED_LEFT = false if event.which is 1
			Variables.MOUSE_PRESSED_MIDDLE = false if event.which is 2
			Variables.MOUSE_PRESSED_RIGHT = false if event.which is 3
		Eventbus.controlsChanged.dispatch event
		Variables.MOUSE_MOVED = false unless Variables.MOUSE_PRESSED_LEFT or Variables.MOUSE_PRESSED_MIDDLE or Variables.MOUSE_PRESSED_RIGHT
		
	onMouseDown: (event) ->
		if event
			Variables.MOUSE_PRESSED_LEFT = true if event.which is 1
			Variables.MOUSE_PRESSED_MIDDLE = true if event.which is 2
			Variables.MOUSE_PRESSED_RIGHT = true if event.which is 3
		Eventbus.controlsChanged.dispatch event
		Variables.MOUSE_MOVED = false
		
	onMouseMove: (event) ->
		if event
			Variables.MOUSE_POSITION_X = event.clientX
			Variables.MOUSE_POSITION_Y = event.clientY
		Eventbus.controlsChanged.dispatch event
		Variables.MOUSE_MOVED = true
	
	onMouseWheel: (event) ->
		Eventbus.controlsChanged.dispatch event
		
	onDOMMouseScroll: (event) ->
		Eventbus.controlsChanged.dispatch event

	onTouchStart: (event) ->
		Eventbus.controlsChanged.dispatch event

	onTouchEnd: (event) ->
		Eventbus.controlsChanged.dispatch event

	onTouchMove: (event) ->
		Eventbus.controlsChanged.dispatch event

return Input