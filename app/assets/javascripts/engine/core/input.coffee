Eventbus = require 'eventbus'
Variables = require 'variables'
log = require 'util/logger'
events = require 'events'
engine = require 'engine'
db = require 'database'

class Input

	constructor: ->
		@views = db.get 'ui/viewports'
		@model = db.get 'input'
		@initialize()
		@addEventListener()
	
	initialize: ->
	
	addEventListener: ->
		log.debug 'Register input listeners'
		domElement = @views.at(0).get 'domElement'
		window.addEventListener 'keyup', (event) => @onKeyUp event
		window.addEventListener 'keydown', (event) => @onKeyDown event
		domElement.addEventListener 'mouseup', (event) => @onMouseUp event
		domElement.addEventListener 'mousedown', (event) => @onMouseDown event
		domElement.addEventListener 'mousemove',(event) => @onMouseMove event
		domElement.addEventListener 'mousewheel', (event) => @onMouseWheel event
		domElement.addEventListener 'DOMMouseScroll', (event) => @onDOMMouseScroll event
		domElement.addEventListener 'touchstart', (event) => @onTouchStart event
		domElement.addEventListener 'touchend', (event) => @onTouchEnd event
		domElement.addEventListener 'touchmove', (event) => @onTouchMove event

	onKeyUp: (event) ->
		Eventbus.controlsChanged.dispatch event

	onKeyDown: (event) ->
		Eventbus.controlsChanged.dispatch event 
		
	onMouseUp: (event) ->
		if event
			@model.set 'mouse_pressed_left', false if event.which is 1
			@model.set 'mouse_pressed_middle', false if event.which is 2
			@model.set 'mouse_pressed_right', false if event.which is 3
		events.trigger 'mouse:up', event
		Eventbus.controlsChanged.dispatch event
		@model.set 'mouse_moved', false unless @model.get('mouse_pressed_left') or @model.get('mouse_pressed_middle') or @model.get('mouse_pressed_right')
		
	onMouseDown: (event) ->
		if event
			@model.set 'mouse_pressed_left', true if event.which is 1
			@model.set 'mouse_pressed_middle', true if event.which is 2
			@model.set 'mouse_pressed_right', true if event.which is 3
		events.trigger 'mouse:down', event
		Eventbus.controlsChanged.dispatch event
		@model.set 'mouse_moved', false
		
	onMouseMove: (event) ->
		if event
			@model.set 
				'mouse_position_x': event.clientX
				'mouse_position_y': event.clientY
		events.trigger 'mouse:move', event
		Eventbus.controlsChanged.dispatch event
		@model.set 'mouse_moved', true
	
	onMouseWheel: (event) ->
		@model.trigger 'mouse:wheel', event
		Eventbus.controlsChanged.dispatch event
		
	onDOMMouseScroll: (event) ->
		@model.trigger 'mouse:wheel', event
		Eventbus.controlsChanged.dispatch event

	onTouchStart: (event) ->
		Eventbus.controlsChanged.dispatch event

	onTouchEnd: (event) ->
		Eventbus.controlsChanged.dispatch event

	onTouchMove: (event) ->
		Eventbus.controlsChanged.dispatch event

return Input