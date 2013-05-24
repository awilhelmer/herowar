AnimatedModel = require 'models/animatedModel'
scenegraph = require 'scenegraph'

class Tower extends AnimatedModel
	
	active: false
	
	range: 100
	
	meshRange: null
	
	target: null
	
	update: (delta) ->
		if @active
			target = null if target and target.object3d.position.distanceTo(@object3d.position) > @range
			target = @_getNextTarget() unless target
			@rotateTo target, delta if target
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

	_getNextTarget: ->
		Enemy = require 'models/enemy'
		for id, obj of scenegraph.dynamicObjects
			# TODO: Improve calculation of best target and do it serverside ;)
			return obj.object3d.position if obj instanceof Enemy and obj.object3d.position.distanceTo(@object3d.position) <= @range
		return

return Tower