MeshModel = require 'models/mesh'

class LaserModel extends MeshModel
	
	rotationMultipler: 50
	
	moveSpeed: 150
	
	distanceToDispose: 10
	
	glowColor: 0xffa500
		
	constructor: (@id, @owner, @target, @damage) ->
		@meshBody = @createMeshBody()
		super @id, "Shot-#{@id}", @meshBody
		@target.damageIncoming += @damage

	createMeshBody: ->
		geometry = new THREE.CubeGeometry 1, 1, 8.5
		material = new THREE.MeshBasicMaterial color: 0xFFA500, transparent: true, opacity: 0.7
		mesh = new THREE.Mesh geometry, material
		mesh.userData.glowing = true
		return mesh

	update: (delta, now) ->
		targetPosition = @target.getMainObject().position.clone()
		#targetPosition.y = @meshBody.position.y
		distance = @getMainObject().position.distanceTo targetPosition
		if distance > @distanceToDispose + @meshBody.position.y
			@rotateTo targetPosition, delta
			@move delta
		else
			@target.hit @damage
			@dispose()

return LaserModel