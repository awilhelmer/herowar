AnimatedModel = require 'models/animatedModel'

class Tower extends AnimatedModel
	
	active: false
	
	range: 100
	
	meshRange: null
	
	update: (delta) ->
		if @active
			
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
				geometry.vertices.push new THREE.Vertex new THREE.Vector3 Math.cos(segment) * amplitude, 0, Math.sin(segment) * amplitude
		  @meshRange = new THREE.Line geometry, material
		  @meshRange.name = 'range'
		  @object3d.add @meshRange
		@meshRange.visible = true

	hideRange: ->
		@meshRange.visible = false if @meshRange

return Tower