class Particle
	
	position: new THREE.Vector3(-10000, -10000, -10000)
	
	velocity: new THREE.Vector3()
	
	force: new THREE.Vector3()
	
	color: new THREE.Color 0x000000
	
	basecolor: new THREE.Color 0x000000
	
	life: 0.0
	
	available: true
	
	reset: ->
		@position.set 0, -100000, 0
		@velocity.set 0, 0, 0
		@force.set 0, 0, 0
		@color.setRGB 0, 0, 0
		@basecolor.setRGB 0, 0, 0
		@life = 0.0
		@available = true

return Particle