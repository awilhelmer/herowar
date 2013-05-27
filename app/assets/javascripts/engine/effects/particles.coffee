Particle = require 'effects/particle'

class Particles
	
	defaultOpts: 
		color           : 0xFFFFFF
		map             : null
		size            : 4
		blending        : THREE.AdditiveBlending
		depthTest       : false
		transparent     : true
		vertexColors    : true
		opacity         : 1.0
		sizeAttenuation : true
		max             : 1000
		spawnRate       : 0
		spawn           : new THREE.Vector3()
		velocity        : new THREE.Vector3()
		randomness      : new THREE.Vector3()
		force           : new THREE.Vector3()
		spawnRadius     : new THREE.Vector3()
		life            : 60
		friction        : 1.0
		position        : new THREE.Vector3()
		rotation        : new THREE.Vector3()
		sort            : false
	
	constructor: (opts) ->
		@opts = _.extend {}, opts
		_.defaults @opts, defaultOpts
		@opts.ageing = 1 / @opts.life
		@initialize()		
	
	initialize: ->
		@color = new THREE.Color @opts.color
		@color2 = if @opts.color2 then new THREE.Color @opts.color2 else null
		@black = new THREE.Color 0x000000
		@white = new THREE.Color 0xFFFFFF
		@material = new THREE.ParticleBasicMaterial _.pick @opts, 'color', 'map', 'size', 'blending', 'depthTest', 'transparent', 'vertexColors', 'opacity', 'sizeAttenuation'
		@build()
	
	build: ->
		@geometry = new THREE.Geometry()
		@geometry.dynamic = true
		@pool = []
		@buffer = []
	
return Particles