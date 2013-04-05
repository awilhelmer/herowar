EditorBindings = require 'ui/bindings'
EditorScenegraph = require 'ui/panel/scenegraph'
Camera = require 'ui/camera'
Eventbus = require 'eventbus'
Variables = require 'variables'
	
class Editor

	constructor: (@app) ->

	init: ->
		@mousePressed = false
		@mouseMoved = false
		@ray = new THREE.Raycaster()
		@projector = new THREE.Projector()
		@camera = new Camera(@)
		@editorBindings = new EditorBindings(@)
		@editorBindings.init()
		@editorScenegraph = new EditorScenegraph(@)
		@addEventListeners(@)

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

	renderer: ->
		@app.engine.renderer

	scenegraph: ->
		@app.engine.scenegraph

	render: ->
		@app.engine.render()

	engine: ->
		@app.engine
	
	onMouseUp: (event) ->
		console.log 'mouseup'
		if event?.button is 0 and !@mouseMoved
			@handleObjectSelection event
		@dispatchControlsChangedEvent event
		@camera.update()
		@mousePressed = false
		@mouseMoved = false
	
	onMouseDown: (event) ->
		console.log 'mousedown'
		@dispatchControlsChangedEvent event
		@camera.update()
		@mousePressed = true
		
	onMouseMove: (event) ->
		if @mousePressed
			console.log 'mousemove'
			@dispatchControlsChangedEvent event
			@camera.update()
			@mouseMoved = true

	handleObjectSelection: (event) ->
		objects = @intersectObjects event
		if objects.length > 0
			@editorScenegraph.handleSelection objects[0].object
		else
			@editorScenegraph.handleSelection()

	intersectObjects: (event) ->
		vector = new THREE.Vector3(
		  ((event.clientX - Variables.SCREEN_LEFT) / Variables.SCREEN_WIDTH) * 2 - 1
		  -((event.clientY - Variables.SCREEN_TOP) / Variables.SCREEN_HEIGHT) * 2 + 1
		  0.5)
		camera = @engine().viewhandler.views[0].camera	# TODO: find out from which viewport this click comes
		@projector.unprojectVector vector, camera
		@ray.set camera.position, vector.sub(camera.position).normalize()
		objects = []
		objects.push obj for id, obj of @engine().scenegraph.dynamicObjects
		objects.push @engine().scenegraph.getMap()
		@ray.intersectObjects objects, true

	dispatchControlsChangedEvent: (event) ->
		Eventbus.controlsChanged.dispatch event

return Editor