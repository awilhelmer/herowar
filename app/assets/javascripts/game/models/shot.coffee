MeshModel = require 'models/mesh'

class ShotModel extends MeshModel
	
	rotationMultipler: 50
	
	moveSpeed: 150
	
	distanceToDispose: 4
	
	constructor: (@id, @owner, @target, @damage) ->
		geometry = new THREE.CubeGeometry 1, 10, 1
		material = new THREE.MeshBasicMaterial color: 0xf43b3c, opacity: 0.5
		@meshBody = @createMeshBody()
		super @id, "Shot-#{@id}", @meshBody
		@object3d.position.set @owner.object3d.position.x, @owner.object3d.position.y, @owner.object3d.position.z
		@object3d.quaternion.setFromRotationMatrix @owner.object3d.matrix

	createMeshBody: ->
		geometry = new THREE.CubeGeometry 1, 10, 1
		material = new THREE.MeshBasicMaterial color: 0xf43b3c, opacity: 0.5
		body = new THREE.Object3D()
		meshShot1 = new THREE.Mesh geometry, material
		meshShot1.position.x = +9
		meshShot1.position.y = +12
		body.add meshShot1
		meshShot2 = new THREE.Mesh geometry, material
		meshShot2.position.x = -9
		meshShot2.position.y = +12
		body.add meshShot2
		body.rotation.x = Math.PI/2
		return body
		

	update: (delta) ->
		distance = @object3d.position.distanceTo @target.object3d.position
		if distance > @distanceToDispose
			@rotateTo @target.object3d.position, delta
			@object3d.translateZ delta * @moveSpeed
		else
			@dispose()

return ShotModel