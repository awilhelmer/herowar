EditorEventbus = require 'editorEventbus'
Variables = require 'variables'
Constants = require 'constants'
Eventbus = require 'eventbus'
Input = require 'core/input'
events = require 'events'
db = require 'database'

class EditorInput extends Input
	
	initialize: ->
		@model = db.get 'input'
		@tool = db.get 'ui/tool'

	onKeyUp: (event) ->
		EditorEventbus.keyup.dispatch event
		super event

	onKeyDown: (event) ->
		EditorEventbus.keydown.dispatch event
		super event

	onMouseUp: (event) ->
		if event
			@model.set 'mouse_pressed_left', false if event.which is 1
			@model.set 'mouse_pressed_middle', false if event.which is 2
			@model.set 'mouse_pressed_right', false if event.which is 3
		events.trigger 'mouse:up', event
		EditorEventbus.mouseup.dispatch event
		unless _.has @tool.get('active'), Constants.TOOL_BRUSH
			Eventbus.controlsChanged.dispatch event
		@model.set 'mouse_moved', false unless @model.get('mouse_pressed_left') or @model.get('mouse_pressed_middle') or @model.get('mouse_pressed_right')
		
	onMouseDown: (event) ->
		if event
			@model.set 'mouse_pressed_left', true if event.which is 1
			@model.set 'mouse_pressed_middle', true if event.which is 2
			@model.set 'mouse_pressed_right', true if event.which is 3
		events.trigger 'mouse:down', event
		EditorEventbus.mousedown.dispatch event
		unless _.has @tool.get('active'), Constants.TOOL_BRUSH
			Eventbus.controlsChanged.dispatch event
		@model.set 'mouse_moved', false
		
	onMouseMove: (event) ->
		if event
			@model.set 
				'mouse_position_x': event.clientX
				'mouse_position_y': event.clientY
		events.trigger 'mouse:move', event
		EditorEventbus.mousemove.dispatch event
		#TODO check if a Tool isSelected - keyshortcut for deselecting tool for good camerahandling
		unless _.has(@tool.get('active'), Constants.TOOL_BRUSH) and (@model.get('mouse_pressed_left') or @model.get('mouse_pressed_right'))
			Eventbus.controlsChanged.dispatch event
		@model.set 'mouse_moved', true
	
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