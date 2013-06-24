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
    @registerInput domElement, 'contextmenu', @onContextMenu
    @registerInput domElement, 'mouseup', @onMouseUp
    @registerInput domElement, 'mousedown', @onMouseDown
    @registerInput domElement, 'mousemove', @onMouseMove
    @registerInput domElement, 'mousewheel', @onMouseWheel
    @registerInput domElement, 'DOMMouseScroll', @onMouseWheel
    @registerInput window, 'keydown', @onKeyDown
    @registerInput window, 'keyup', @onKeyUp

	onContextMenu: (event) =>
		return unless event
		event.preventDefault()
		return

	onMouseUp: (event) =>
		return unless event
		@input.set 'mouse_pressed_left', false if event.which is 1
		@input.set 'mouse_pressed_middle', false if event.which is 2
		@input.set 'mouse_pressed_right', false if event.which is 3
		events.trigger 'mouse:up', event
		@input.set 'mouse_moved', false unless @input.get('mouse_pressed_left') or @input.get('mouse_pressed_middle') or @input.get('mouse_pressed_right')
		return
		
	onMouseDown: (event) =>
		return unless event
		@input.set 'mouse_pressed_left', true if event.which is 1
		@input.set 'mouse_pressed_middle', true if event.which is 2
		@input.set 'mouse_pressed_right', true if event.which is 3
		@input.trigger 'mouse:down', event
		@input.set 'mouse_moved', false
		return

	onMouseMove: (event) =>
		return unless event
		@input.set 
			'mouse_position_x' : event.clientX
			'mouse_position_y' : event.clientY
			'mouse_moved'      : true
		events.trigger 'mouse:move', event
		return
	
	onMouseWheel: (event) =>
		return unless event
		delta = if event.wheelDelta then event.wheelDelta else if event.detail then -event.detail else 0
		@changeZoom 0.1 if delta > 0
		@changeZoom -0.1 if delta < 0
		return

	onKeyDown: (event) =>
		return unless event
		@keysPressed.push event.keyCode if event.keyCode and _.indexOf(@keysPressed, event.keyCode) is -1
		@input.trigger 'key:down', event
		#console.log 'onKeyUp() Active keys pressed: ', @keysPressed
		return
		
	onKeyUp: (event) =>
		return unless event
		@keysPressed.splice _.indexOf(@keysPressed, event.keyCode), 1 if event.keyCode and _.indexOf(@keysPressed, event.keyCode) isnt -1
		@input.trigger 'key:up', event
		#console.log 'onKeyUp() Active keys pressed: ', @keysPressed
		return

	changeZoom: (value) ->
		camera = @view.get 'camera'
		cameraScene = @view.get 'cameraScene'
		if cameraScene instanceof THREE.OrthographicCamera
			@zoom += value
			#console.log 'RTSControls changeZoom()', value, 'to', @zoom
		else
			maxPos = 800 #TODO what is that? 
			minPos = 100
			camera.position[1] = Math.round(camera.position[1] - value * 100)
			camera.position[1] = maxPos if camera.position[1] > maxPos
			camera.position[1] = minPos if camera.position[1] < minPos
			camera.rotation[0] = THREE.Math.degToRad((camera.position[1] - minPos) / (maxPos - minPos) * -90)
			#console.log 'RTSControls changeZoom()', value, 'to', camera.position[1]
		@view.trigger 'change:camera'
		@view.trigger 'change'
		@view.updateCamera()
		return

	update: ->
		return unless @view or @camera
		scrollLeft = _.indexOf(@keysPressed, @KEY.LEFT) isnt -1
		scrollUp = _.indexOf(@keysPressed, @KEY.UP) isnt -1
		scrollRight = _.indexOf(@keysPressed, @KEY.RIGHT) isnt -1
		scrollDown = _.indexOf(@keysPressed, @KEY.DOWN) isnt -1
		if scrollLeft or scrollUp or scrollRight or scrollDown
			camera = @view.get 'camera'
			cameraScene = @view.get 'cameraScene'
			if cameraScene instanceof THREE.OrthographicCamera
				#camera.offset.left -= 1 if scrollLeft and camera.offset.left isnt 0
				#camera.offset.left += 1 if scrollRight
				#camera.offset.top -= 1 if scrollUp and camera.offset.top isnt 0
				#camera.offset.top += 1 if scrollDown
				camera.position[0] -= 1 if scrollLeft
				camera.position[0] += 1 if scrollRight
				camera.position[2] -= 1 if scrollUp
				camera.position[2] += 1 if scrollDown
				#console.log 'Camera Update', camera.position, cameraScene.left, cameraScene.right, cameraScene.top, cameraScene.bottom
			else
				camera.position[0] -= 1 if scrollLeft
				camera.position[0] += 1 if scrollRight
				camera.position[2] -= 1 if scrollUp
				camera.position[2] += 1 if scrollDown
			@view.trigger 'change:camera'
			@view.trigger 'change'
			@view.updateCamera()
		return

return RTSControls