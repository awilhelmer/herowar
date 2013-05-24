MeshModel = require 'models/mesh'

class LaserModel extends MeshModel
	
	rotationMultipler: 50
	
	moveSpeed: 150
	
	distanceToDispose: 10
	
	constructor: (@id, @owner, @target, @damage) ->
		geometry = new THREE.CubeGeometry 1, 10, 1
		material = new THREE.MeshBasicMaterial color: 0xf43b3c, opacity: 0.5
		@meshBody = @createMeshBody()
		super @id, "Shot-#{@id}", @meshBody
		@root.position.set @owner.root.position.x, @owner.root.position.y, @owner.root.position.z
		@root.quaternion.setFromRotationMatrix @owner.root.matrix

	createMeshBody: ->
		geometry = new THREE.CubeGeometry 0.5, 7.5, 0.5
		material = new THREE.MeshBasicMaterial color: 0xf43b3c, opacity: 0.5
		body = new THREE.Object3D()
		shot = new THREE.Mesh geometry, material
		body.add shot
		body.rotation.x = Math.PI/2
		return body
		

	update: (delta) ->
		distance = @root.position.distanceTo @target.root.position
		if distance > @distanceToDispose
			@rotateTo @target.root.position, delta
			@root.translateZ delta * @moveSpeed
		else
			@dispose()

return LaserModel