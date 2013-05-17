AnimatedModel = require 'models/animatedModel'

class Tower extends AnimatedModel
	
	active: false
	
	meshRange: null
	
	update: (delta) ->
		super delta if @active

	showRange: ->
		unless @meshRange
			resolution = 100
			amplitude = 100
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