EditorEventbus = require 'editorEventbus'
Eventbus = require 'eventbus'
Variables = require 'variables'
Constants = require 'constants'
log = require 'util/logger'
db = require 'database'

class Input

	constructor: (@editor) ->
		@initialize()
	
	initialize: ->
		@addEventListener()
		@tool = db.get 'ui/tool'
	
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
		EditorEventbus.keyup.dispatch event
		Eventbus.controlsChanged.dispatch event

	onKeyDown: (event) ->
		EditorEventbus.keydown.dispatch event
		Eventbus.controlsChanged.dispatch event 
		null

	onMouseUp: (event) ->
		if event
			Variables.MOUSE_PRESSED_LEFT = false if event.which is 1
			Variables.MOUSE_PRESSED_MIDDLE = false if event.which is 2
			Variables.MOUSE_PRESSED_RIGHT = false if event.which is 3
		EditorEventbus.mouseup.dispatch event
		unless @tool.get('active') is Constants.TOOL_BRUSH
			Eventbus.controlsChanged.dispatch event
		Variables.MOUSE_MOVED = false unless Variables.MOUSE_PRESSED_LEFT or Variables.MOUSE_PRESSED_MIDDLE or Variables.MOUSE_PRESSED_RIGHT
		
	onMouseDown: (event) ->
		if event
			Variables.MOUSE_PRESSED_LEFT = true if event.which is 1
			Variables.MOUSE_PRESSED_MIDDLE = true if event.which is 2
			Variables.MOUSE_PRESSED_RIGHT = true if event.which is 3
		EditorEventbus.mousedown.dispatch event
		unless @tool.get('active') is Constants.TOOL_BRUSH
			Eventbus.controlsChanged.dispatch event
		Variables.MOUSE_MOVED = false
		
	onMouseMove: (event) ->
		if event
			Variables.MOUSE_POSITION_X = event.clientX
			Variables.MOUSE_POSITION_Y = event.clientY
		EditorEventbus.mousemove.dispatch event
		#TODO check if a Tool isSelected - keyshortcut for deselecting tool for good camerahandling
		if @tool.get('active') isnt Constants.TOOL_BRUSH and (Variables.MOUSE_PRESSED_LEFT or Variables.MOUSE_PRESSED_RIGHT)
			Eventbus.controlsChanged.dispatch event
		Variables.MOUSE_MOVED = true
	
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