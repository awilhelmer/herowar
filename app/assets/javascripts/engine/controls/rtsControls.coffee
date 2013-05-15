InputEvents = require 'controls/inputEvents'
events = require 'events'
db = require 'database'

class RTSControls
	
	constructor: (@view) ->
		_.extend @, Backbone.Events, InputEvents
		@initialize()
	
	initialize: ->
		@input = db.get 'input'
		@initVariables()
		@bindEvents()
		@addInputListeners @view
		return
	
	initVariables: ->
		@camera = @view.get 'camera'
		@center = new THREE.Vector3()
		@userZoomSpeed = 1.0
		@zoom = 1.0
		@lastPosition = new THREE.Vector3()
		return
	
	bindEvents: ->
		events.on 'engine:render', @update, @
		events.on 'scene:terrain:build', @changeTerrain, @
		#@listenTo @input, 'mouse:wheel', @onMouseWheel
	
	onMouseWheel: (event) ->
		return unless event or @view or @camera
		delta = if event.wheelDelta then event.wheelDelta else if event.detail then -event.detail else 0
		@changeZoom 0.1 if delta > 0
		@changeZoom -0.1 if delta < 0
		return

	changeZoom: (value) ->
		@zoom += value
		console.log 'RTSControls changeZoom()', value, 'to', @zoom
		return

	changeTerrain: (terrain) ->
		console.log 'RTSControls changeTerrain()', terrain
		return

	update: ->
		return unless @view or @camera
		# TODO: implement mouse move logic...
		return

return RTSControls