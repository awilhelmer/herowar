InputEvents = require 'controls/inputEvents'
events = require 'events'
db = require 'database'

class RTSControls
	
	STATE:
		SCROLL_LEFT: 0
		SCROLL_UP: 1
		SCROLL_RIGHT: 2
		SCROLL_DOWN: 3
	
	KEY:
		LEFT: 37
		UP: 38
		RIGHT: 39
		DOWN: 40
	
	constructor: (@view) ->
		_.extend @, Backbone.Events, InputEvents
		@initialize()
	
	initialize: ->
		@input = db.get 'input'
		@initVariables()
		@bindEvents()
		@bindListeners()
		return
	
	initVariables: ->
		@camera = @view.get 'camera'
		@center = new THREE.Vector3()
		@userZoomSpeed = 1.0
		@zoom = 1.0
		@lastPosition = new THREE.Vector3()
		@keysPressed = []
		return
	
	bindEvents: ->
    events.on 'engine:render', @update, @

  bindListeners: ->
    domElement = @view.get 'domElement'
    @registerInput domElement, 'mousewheel', @onMouseWheel
    @registerInput domElement, 'DOMMouseScroll', @onMouseWheel
    @registerInput window, 'keydown', @onKeyDown
    @registerInput window, 'keyup', @onKeyUp
				
	onMouseWheel: (event) =>
		return unless event or @view or @camera
		delta = if event.wheelDelta then event.wheelDelta else if event.detail then -event.detail else 0
		@changeZoom 0.1 if delta > 0
		@changeZoom -0.1 if delta < 0
		return

	onKeyDown: (event) =>
		return unless event
		@keysPressed.push event.keyCode if event.keyCode and _.indexOf(@keysPressed, event.keyCode) is -1
		console.log 'onKeyUp() Active keys pressed: ', @keysPressed
		return
		
	onKeyUp: (event) =>
		return unless event
		@keysPressed.splice _.indexOf(@keysPressed, event.keyCode), 1 if event.keyCode and _.indexOf(@keysPressed, event.keyCode) isnt -1
		console.log 'onKeyUp() Active keys pressed: ', @keysPressed
		return

	changeZoom: (value) ->
		@zoom += value
		console.log 'RTSControls changeZoom()', value, 'to', @zoom
		return

	update: ->
		return unless @view or @camera
		scrollLeft = _.indexOf(@keysPressed, @KEY.LEFT) isnt -1
		scrollUp = _.indexOf(@keysPressed, @KEY.UP) isnt -1
		scrollRight = _.indexOf(@keysPressed, @KEY.RIGHT) isnt -1
		scrollDown = _.indexOf(@keysPressed, @KEY.DOWN) isnt -1
		if scrollLeft or scrollUp or scrollRight or scrollDown
			camera = @view.get 'camera'
			camera.offset.left -= 1 if scrollLeft
			camera.offset.left += 1 if scrollRight
			camera.offset.top -= 1 if scrollUp
			camera.offset.top += 1 if scrollDown
			console.log 'Camera Offset', camera.offset
			@view.updateSize()
		return

return RTSControls