EditorEventbus = require 'editorEventbus'
Variables = require 'variables'
Constants = require 'constants'
Eventbus = require 'eventbus'
Input = require 'core/input'
db = require 'database'

class EditorInput extends Input
	
	initialize: ->
		@tool = db.get 'ui/tool'

	onKeyUp: (event) ->
		EditorEventbus.keyup.dispatch event
		super event

	onKeyDown: (event) ->
		EditorEventbus.keydown.dispatch event
		super event

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
		super event

		
	onDOMMouseScroll: (event) ->
		EditorEventbus.domMouseScroll.dispatch event
		super event

	onTouchStart: (event) ->
		EditorEventbus.touchstart.dispatch event
		super event

	onTouchEnd: (event) ->
		EditorEventbus.touchend.dispatch event
		super event

	onTouchMove: (event) ->
		EditorEventbus.touchmove.dispatch event
		super event

return EditorInput