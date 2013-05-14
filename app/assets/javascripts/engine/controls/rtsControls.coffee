events = require 'events'
db = require 'database'

class RTSControls
	
	constructor: (@view) ->
		_.extend @, Backbone.Events
		@initialize()
	
	initialize: ->
		@input = db.get 'input'
		@initVariables()
		@bindEvents()
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
		@listenTo @input, 'mouse:wheel', @onMouseWheel
	
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
		###
		position = @camera.position
		offset = position.clone().sub @center
		
		theta = Math.atan2 offset.x, offset.z
		phi = Math.atan2 Math.sqrt(offset.x * offset.x + offset.z * offset.z), offset.y
		
		radius = offset.length() * @scale
		radius = Math.max @minDistance, Math.min @maxDistance, radius
		
		offset.x = Math.round radius * Math.sin(phi) * Math.sin(theta)
		offset.y = Math.round radius * Math.cos(phi)
		offset.z = Math.round radius * Math.sin(phi) * Math.cos(theta)
		
		@camera.position.copy(@center).add offset
		
		@scale = 1
		
		if @lastPosition.distanceTo(@camera.position) > 0
			events.trigger 'views:camera:changed'
			@view.get('position')[0] = @camera.position.x
			@view.get('position')[1] = @camera.position.y
			@view.get('position')[2] = @camera.position.z
			@view.trigger 'change:position'
			@view.trigger 'change'
			@lastPosition.copy @camera.position
		###
		return

return RTSControls