events = require 'events'
db = require 'database'

class RTSControls
	
	constructor: ->
		_.extend @, Backbone.Events
		@initialize()
	
	initialize: ->
		@input = db.get 'input'
		@initVariables()
		@bindEvents()
		return
	
	initVariables: ->
		@view = null
		@camera = null
		@center = new THREE.Vector3()
		@userZoomSpeed = 1.0
		@scale = 1
		@minDistance = 100
		@maxDistance = 1000
		@lastPosition = new THREE.Vector3()
		return
	
	bindEvents: ->
		events.on 'engine:render:before', @update, @
		events.on 'controls:rts:view', @changeView, @
		@listenTo @input, 'mouse:wheel', @onMouseWheel
	
	onMouseWheel: (event) ->
		delta = 0
		if event.wheelDelta # WebKit / Opera / Explorer 9
			delta = event.wheelDelta
		else if event.detail # Firefox
			delta = - event.detail
		if delta > 0
			@zoomOut()
		else
			@zoomIn()
		return

	zoomIn: (zoomScale) ->
		zoomScale = @getZoomScale() unless zoomScale
		@scale /= zoomScale
		return

	zoomOut: (zoomScale) ->
		zoomScale = @getZoomScale() unless zoomScale
		@scale *= zoomScale
		return

	getZoomScale: ->
		Math.pow 0.95, @userZoomSpeed

	changeView: (view) ->
		@view = view
		@camera = view.get 'camera'
		return

	update: ->
		return unless @view or @camera
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
		return

return RTSControls