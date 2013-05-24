AnimatedModel = require 'models/animatedModel'
scenegraph = require 'scenegraph'

class Tower extends AnimatedModel
	
	active: false
	
	range: 100
	
	meshRange: null
	
	target: null
	
	update: (delta) ->
		if @active
			@_rotateToTarget delta
			super delta

	showRange: ->
		unless @meshRange
			resolution = @range
			amplitude = @range
			size = 360 / resolution
			geometry = new THREE.Geometry()
			material = new THREE.LineBasicMaterial color: 0x000000, opacity: 1.0
			for i in [0..resolution]
				segment = (i * size) * Math.PI / 180
				geometry.vertices.push new THREE.Vector3 Math.cos(segment) * amplitude, 0, Math.sin(segment) * amplitude
		  @meshRange = new THREE.Line geometry, material
		  @meshRange.name = 'range'
		  @object3d.add @meshRange
		@meshRange.visible = true

	hideRange: ->
		@meshRange.visible = false if @meshRange

	_rotateToTarget: (delta) ->
		@rotateTo @target.object3d.position, delta if @_isTargetInRange()

	_isTargetInRange: ->
		return if @target and @target.object3d.position.distanceTo(@object3d.position) <= @range then true else false

return Tower